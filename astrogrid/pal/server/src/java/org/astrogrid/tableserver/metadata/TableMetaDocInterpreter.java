/*
 * $Id: TableMetaDocInterpreter.java,v 1.16 2007/06/08 13:16:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.xml.DomHelper;
//import org.astrogrid.dataservice.metadata.XmlTypes;
import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;

/**
 * Provides a set of convenience routines for accessing the TableMetaDoc that
 * defines tabular datasets.  Generally speaking we want to
 * return details from the document instead of the database itself for two 
 * reaons:
 * 1) The database may not be locally available (if it's proxied) 
 *    or it might not really exist - the service may be simulating one
 * 2) The sysadmin will have decided which tables/columns are to be published
 *    and which are not, and the ones to be published are in the resource 
 *    document
 * 4) There are many extra bits of information that we may need to know 
 *    (eg units) that are not available from the db's natural metadata
 *
 *  IMPORTANT NOTE:  Names and IDs in the metadoc are handled in a 
 *  CASE-INSENSITIVE way;  this is necessary because SQL is case-insensitive,
 *  and JDBC plugins may throw back database/table/column names in arbitrary
 *  case (e.g. converted to allcaps).
 */

public class TableMetaDocInterpreter
{
   private static Log log = LogFactory.getLog(TableMetaDocInterpreter.class);
   
   protected static boolean initialized = false;

// Element[] catalogs; //root list of catalogs
   protected static Element metadoc = null;

   protected static URL docUrl = null;
   
   public final static String TABLE_METADOC_URL_KEY = "datacenter.metadoc.url";
   public final static String TABLE_METADOC_FILE_KEY = "datacenter.metadoc.file";
   public final static String ALLOWED_METADOC_NAMESPACES[] =  {
      "urn:astrogrid:schema:dsa:TableMetaDoc:v1.1"
   };
   public final static String METADOC_NAMESPACE_LATEST =
      "urn:astrogrid:schema:dsa:TableMetaDoc:v1.1";

   /** Make the constructor private to prevent instantiating this class. */
   private TableMetaDocInterpreter() throws MetadataException
   {
      throw new MetadataException("Please don't instantiate the TableMetaDocinterpreter class - all its useful methods are static methods.");
   }

   /** Initialize to interpret the table metadoc given in the config file.
    * If "force" is set to true, always re-initializes the metadata. */
   public static void initialize(boolean force) throws MetadataException {
     if ((initialized) && (force == false)) {
        return;  // No need to do anything
     }
     try {
       // Initialise SampleStars plugin if required (may not be initialised
       // if admin has not run the self-tests)
        String plugin = ConfigFactory.getCommonConfig().getString(
            "datacenter.querier.plugin");
         if (plugin.equals(
               "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
            // This has no effect if the plugin is already initialised
            SampleStarsPlugin.initialise();  // Just in case
         }
      }
      catch (DatabaseAccessException dbe) {
         throw new MetadataException(dbe.getMessage());
      }
      docUrl = ConfigFactory.getCommonConfig().getUrl(
            TABLE_METADOC_URL_KEY, null);
      try {
         if (docUrl != null) {
            loadUrl(docUrl);
         }
         else {
            docUrl = ConfigReader.resolveFilename(ConfigFactory.getCommonConfig().getString(TABLE_METADOC_FILE_KEY));
            loadUrl(docUrl);
         }
      }
      catch (IOException ioe) {
         throw new MetadataException(ioe.getMessage());
      }
      initialized = true;
   }

   public static boolean isValid() {
      try {
         initialize(false);
      }
      catch (Exception e) {
         return false;
      }
      if (metadoc == null) {
         return false;
      }
      return true;
   }

   /** Returns the list of IDs of all the catalogs in the metadoc */
   public static String[] getCatalogIDs() throws MetadataException  
   {
      initialize(false);

      Element[] elements = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      String[] catIDs = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catIDs[i] = elements[i].getAttribute("ID");
      }
      return catIDs;
   }

   /** Returns the list of names of all the catalogs in the metadoc */
   public static String[] getCatalogNames() throws MetadataException  
   {
      initialize(false);
      Element[] elements = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      String[] catNames = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catNames[i] = DomHelper.getValueOf(elements[i], "Name");
      }
      return catNames;
   }

   /** Returns the list of descriptions of all the catalogs in the metadoc */
   public static String[] getCatalogDescriptions() throws MetadataException  
   {
      initialize(false);
      Element[] elements = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      String[] catDescs = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catDescs[i] = DomHelper.getValueOf(elements[i], "Description");
      }
      return catDescs;
   }

   /** Return the Name associated with a particular catalog ID */
   public static String getCatalogNameForID(String catalogID) 
         throws MetadataException 
   {
      Element catNode = getCatalogElementByID(catalogID);
      return DomHelper.getValueOf(catNode, "Name");
   }
   /** Return the ID associated with a particular catalog Name */
   public static String getCatalogIDForName(String catalogName) 
         throws MetadataException 
   {
      Element catNode = getCatalogElementByName(catalogName);
      return catNode.getAttribute("ID");
   }


   /** Returns all conesearchable tables.  */
   public static TableInfo[] getConesearchableTables() throws MetadataException 
   {
      Vector vectorOfInfos = new Vector();
      initialize(false);
      // Get all catalogs
      Element[] catalogs = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      for (int i = 0; i < catalogs.length; i++) {
         // Get all tables
         Element[] tables = DomHelper.getChildrenByTagName(
              catalogs[i], "Table");
         for (int j = 0; j < tables.length; j++) {
            TableInfo info = makeTableInfo(tables[j]);
            if (info.getConesearchable() == true) {
               String catalogID = catalogs[i].getAttribute("ID");
               info.setCatalog(getCatalogNameForID(catalogID),catalogID);
               vectorOfInfos.add(info);
            }
         }
      }
      TableInfo[] infoArray = new TableInfo[vectorOfInfos.size()];
      return (TableInfo[])vectorOfInfos.toArray(infoArray);
   }

   /** Return table info on the table with the given ID.  */
   public static TableInfo getTableInfoByID(String catalogID, String tableID) 
         throws MetadataException 
   {
      TableInfo info = makeTableInfo(getTableElementByID(
               getCatalogElementByID(catalogID), tableID));
      info.setCatalog(getCatalogNameForID(catalogID), catalogID);
      return info;
   }

   /** Return table info on the table with the given Name*/
   public static TableInfo getTableInfoByName(String catalogName, 
         String tableName) throws MetadataException 
   {
      TableInfo info = makeTableInfo(getTableElementByName(
               getCatalogElementByName(catalogName), tableName));
      info.setCatalog(catalogName, getCatalogIDForName(catalogName));
      return info;
   }

   /** Return the Name associated with a particular table ID */
   public static String getTableNameForID(String catalogID, String tableID) 
       throws MetadataException
   {
      Element catNode = getCatalogElementByID(catalogID);
      Element tableNode = getTableElementByID(catNode, tableID);
      return DomHelper.getValueOf(tableNode, "Name");
   }
   /** Return the ID associated with a particular table Name */
   public static String getTableIDForName(String catalogName, String tableName)
         throws MetadataException 
   {
      Element catNode = getCatalogElementByName(catalogName);
      Element tableNode = getTableElementByName(catNode, tableName);
      return tableNode.getAttribute("ID");
   }

   /** Return the ID associated with a particular table Name, when
    * we don't know the name of the parent catalog.
    * KONA TOFIX: hopefully this method can be deprecated once the 
    * ADQL queries are embedding catalog as well as table names. */
   public static String guessTableIDForName(String tableName)
         throws MetadataException 
   {
      String foundID = null;  // Don't know what the ID is
      Element[] cats = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      for (int i = 0; i < cats.length; i++) {
         Element tableNode = null;
         try {
            tableNode = getTableElementByName(cats[i], tableName);
         }
         catch (MetadataException mde) {
            // Didn't find it in the catalog being examined 
         }
         if (tableNode != null) {
           if (foundID != null) {
               // Oh dear, we have found a second potential match
               // in a different catalog
                throw new TooManyTablesException( "Table with name '"+
                      tableName+"' found in more than one catalog");
            }
            foundID = tableNode.getAttribute("ID");
         } 
      }
      if (foundID == null) { // Didn't find one
         throw new MetadataException( "Table with name '"+
               tableName+ "' not found in any catalog");
      }
      return foundID;   // We found it 
   }

   /** Creates an array of TableInfo instances representing the 
    * tables in the specified catalog, selected by catalog ID.
    */
   public static TableInfo[] getTablesInfoByID(String catalogID) 
      throws MetadataException 
   {
      initialize(false);
      Element[] elements = DomHelper.getChildrenByTagName(getCatalogElementByID(catalogID), "Table");
      TableInfo[] infos = new TableInfo[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         infos[i] = makeTableInfo(elements[i]);
         infos[i].setCatalog(getCatalogNameForID(catalogID),catalogID);
      }
      return infos;
   }

   /** Creates an array of TableInfo instances representing the 
    * tables in the specified catalog, selected by catalog Name.
    */
   public static TableInfo[] getTablesInfoByName(String catalogName) 
      throws MetadataException 
   {
      String catID = getCatalogIDForName(catalogName);
      return getTablesInfoByID(catID);
   }


   /** Return column info for the column with the given ID in the given 
    * table and catalog IDs */
   public static ColumnInfo getColumnInfoByID(String catalogID, String tableID, 
         String columnID) throws MetadataException 
   {
      return makeColumnInfo(catalogID,
            tableID, getColumnElementByID(catalogID, tableID, columnID));
   }

   /** Return column info for the column with the given Name in the given 
    * table and catalog Names */
   public static ColumnInfo getColumnInfoByName(String catalogName, 
         String tableName, String columnName) throws MetadataException 
   {
      // Translate to IDs, needed by makeColumnInfo
      String catID = getCatalogIDForName(catalogName);
      String tableID = getTableIDForName(catalogName,tableName);
      return makeColumnInfo(catID,
          tableID, getColumnElementByName(catalogName, tableName, columnName));
   }

   /** Return the ID associated with a particular column Name */
   public static String getColumnIDForName(String catalogName, String tableName,          String colName) throws MetadataException 
   {
      Element colNode = getColumnElementByName(catalogName, tableName, colName);
      return colNode.getAttribute("ID");
   }
   /** Return the Name associated with a particular column ID */
   public static String getColumnNameForID(String catalogID, String tableID, 
         String colID) throws MetadataException {
      Element colNode = getColumnElementByID(catalogID, tableID, colID);
      return DomHelper.getValueOf(colNode, "Name");
   }


   /** Returns the RA column for a particular conesearchable table */
   public static String getConeRAColumnByName(String catalogName, String tableName)
            throws MetadataException {

      Element tableNode = getTableElementByName(
            getCatalogElementByName(catalogName), tableName);
      Element cone = DomHelper.getSingleChildByTagName(tableNode, "ConeSettings");
      if (cone == null) {
         // Table is not conesearchable!!
         throw new MetadataException("Table '" + tableName + 
               "' in catalog '" + catalogName + 
               "' is not configured for conesearch");
      }
      // Should be valid, checked at load-time
      return (DomHelper.getValueOf(cone,"RAColName").trim());
   }
   /** Returns the Dec column for a particular conesearchable table */
   public static String getConeDecColumnByName(
         String catalogName, String tableName) throws MetadataException 
   {
      Element tableNode = getTableElementByName(
            getCatalogElementByName(catalogName), tableName);
      Element cone = DomHelper.getSingleChildByTagName(
            tableNode, "ConeSettings");
      if (cone == null) {
         // Table is not conesearchable!!
         throw new MetadataException("Table '" + tableName + 
               "' in catalog '" + catalogName + 
               "' is not configured for conesearch");
      }
      // Should be valid, checked at load-time
      return (DomHelper.getValueOf(cone,"DecColName").trim());
   }
   /** Returns the Units for a particular column, sanitychecked to make
    * sure that they are valid conesearch units (deg or rad) */
   public static String getConeUnitsByName(String catalogName, String tableName)
         throws MetadataException 
   {
      String raCol = getConeRAColumnByName(catalogName, tableName);
      Element colNode = getColumnElementByName(catalogName, tableName, raCol);
      String units = DomHelper.getValueOf(colNode,"Units");
      if ( !"deg".equals(units) && !"rad".equals(units) ) {
         //Shouldn't get here, has been prechecked at load time
         throw new MetadataException("Column '" + raCol + "' in table '" + 
               tableName + "' in catalog '" + catalogName + 
               "' does not have expected units of 'deg' or 'rad' - " +
               "found '" +units+"' instead.'");
      }
      return units;
   }


   /** Creates an array of ColumnInfo instances representing the columns
    * in the specified table, selected by catalog and table ID.
    */
   public static ColumnInfo[] getColumnsInfoByID(String catalogID, 
         String tableID) throws MetadataException 
   {
      initialize(false);
      Element tableElement = 
            getTableElementByID(getCatalogElementByID(catalogID), tableID);
      Element[] elements = 
            DomHelper.getChildrenByTagName(tableElement, "Column");
      ColumnInfo[] infos = new ColumnInfo[elements.length];
      for (int i = 0; i < elements.length; i++) {
         infos[i] = makeColumnInfo(catalogID, tableID, elements[i]);
      }
      return infos;
   }

   /** Creates an array of ColumnInfo instances representing the columns
    * in the specified table, selected by catalog and table Name.
    */
   public static ColumnInfo[] getColumnsInfoByName(String catalogName, 
         String tableName) throws MetadataException 
   {
      // Translate from Names to IDs
      String catID = getCatalogIDForName(catalogName);
      String tableID = getTableIDForName(catalogName, tableName);
      return getColumnsInfoByID(catID, tableID);
   }
   

    /** Returns the element corresponding to the only matching column; if 
    * there are several or none throws an exception.  This method is 
    * used by SqlResults to look in the specified tableNames (extracted
    * from an ADQL query - therefore Names not IDs) for the column with
    * the specfied ID. */
   public static ColumnInfo guessColumn(
         String[] tableNames, String columnID) throws IOException 
   {
      initialize(false);

      Element foundCol = null;
      String foundTableID = null;
      String foundCatID = null;
      String[] localTables = null;

      //loop through all the catalogs
      String[] catIDs = getCatalogIDs();

      for (int d = 0; d < catIDs.length; d++) {
         Element catNode = getCatalogElementByID(catIDs[d]);
         
         //if table(s) not given, use all tables
         if ((tableNames==null) || (tableNames.length == 0)) {
            Element[] realTables = 
                DomHelper.getChildrenByTagName(catNode, "Table");
            localTables = new String[realTables.length];
            for (int i = 0; i < realTables.length; i++) {
               localTables[i] = DomHelper.getValueOf(DomHelper.getSingleChildByTagName(realTables[i], "Name"));
            }
         }
         else {
            localTables = (String[])tableNames.clone();
         }
         for (int t = 0; t < localTables.length; t++) {
            // Check to make sure we haven't got a qualified
            // table name, eg "catalog.table" (although these should
            // have been cleaned up already, so shouldn't get here)
            String fullTableName = localTables[t];
            String tableName = fullTableName;
            String catalogName = "";
            /*
            int dotIndex = fullTableName.indexOf('.');
            if (dotIndex != -1) {   //Dot found
               catalogName = fullTableName.substring(0,dotIndex);
               tableName = fullTableName.substring(dotIndex+1);
            }
            */
            String realCatName = getCatalogNameForID(catIDs[d]);
            if (("".equals(catalogName)) || 
                  (catalogName.equals(realCatName))) {
               // If catalog matches, or have no catalog name
               Element tableElement = null;
               try {
                 tableElement = getTableElementByName(catNode, tableName);
               } 
               catch (MetadataException mde) {
                  //Table not found in this catalog
               }   
               if (tableElement != null) {
                  Element[] cols = DomHelper.getChildrenByTagName(
                        tableElement, "Column");
                  for (int c = 0; c < cols.length; c++) {
                     String colID = cols[c].getAttribute("ID");
                     if (colID.trim().toLowerCase().equals(
                              columnID.toLowerCase())) {
                        if (foundCol == null) {
                           foundCol = cols[c];
                           foundCatID = catIDs[d];
                           foundTableID = getTableIDForName(
                              getCatalogNameForID(foundCatID), tableName);
                        }
                        else {
                           throw new TooManyColumnsException(
                             "Column with ID "+columnID+" found more than once");
                        }
                     }
                  }
               }
            }
         }
      }
      if (foundCol == null) {
         throw new MetadataException(
               "Column with ID "+columnID+" not found in any table");
      }
      return makeColumnInfo(foundCatID, foundTableID, foundCol);
   }




   /** Method to use when an empty catalog ID or Name is given as
    * a parameter.  For now, we will just return the first catalog
    * in the metadoc;  later on, when multi-cat databases are 
    * ready, we may choose to always throw an exception here.
    */
   protected static Element getCatalogForNullParam() throws MetadataException
   {
      Element[] cats = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      if (cats.length > 0) {
         log.debug("Requested null catalog name/ID : defaulting to first catalog specified in metadoc");
         return cats[0];
      }
      throw new MetadataException("Cannot find default catalog to use");
   }

   /** Returns the element describing the catalog (group of tables/DBMS
    * database) with the given ID attribute.
    */
   protected static Element getCatalogElementByID(String catalogID) 
          throws MetadataException 
   {
      initialize(false);
      if ((catalogID == null) || (catalogID.trim().length()==0)) {
         return getCatalogForNullParam();
      }
      Element[] cats = DomHelper.getChildrenByTagName(metadoc, "Catalog");

      for (int i = 0; i < cats.length; i++) {
         String catId = cats[i].getAttribute("ID");
         if ((catId == null) || (catId.trim().length()==0)) { 
           String errorMsg = 
              "Metadoc is invalid, at least one catalog has a " +
              "missing/empty 'ID' attribute.";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         if (catId.trim().toLowerCase().equals(catalogID.toLowerCase())) {
            return cats[i];
         }
      }
      throw new NoSuchCatalogException("Couldn't find catalog with ID '" +
            catalogID + "'");
   }

   /** Returns the element describing the catalog (group of tables/DBMS
    * database) * with the given Name field.
    * If null is given, assumes the first catalog.
    */
   protected static Element getCatalogElementByName(String catalogName) 
         throws MetadataException 
   {
      initialize(false);
      if ((catalogName == null) || (catalogName.trim().length()==0)) {
         return getCatalogForNullParam();
      }
      Element[] cats = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      for (int i = 0; i < cats.length; i++) {
         String catName = DomHelper.getValueOf(cats[i], "Name");
         if ((catName == null) || (catName.trim().length()==0)) { 
           // NB: Schema validation should catch this error earlier
           String errorMsg = 
              "Metadoc is invalid, at least one catalog has a " +
              "missing/empty 'Name' element.";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         if (catName.trim().toLowerCase().equals(catalogName.toLowerCase())) {
            return cats[i];
         }
      }
      throw new NoSuchCatalogException("Couldn't find catalog with Name '" +
            catalogName + "'");
   }

      
   /** Returns the element describing the table with the given ID attribute.
    */
   protected static Element getTableElementByID(Element catalog, String tableID) throws MetadataException
   {
      initialize(false);
      if (tableID == null || tableID.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (catalog == null) { 
         throw new IllegalArgumentException("Null catalog element supplied"); 
      }
      Element[] tables = DomHelper.getChildrenByTagName(catalog, "Table");
      for (int i = 0; i < tables.length; i++) {
         String id = tables[i].getAttribute("ID");
         if ((id == null) || (id.trim().length()==0)) { 
           // NB: Schema validation should catch this error earlier
           String errorMsg = 
              "Metadoc is invalid, at least one table has a " +
              "missing/empty 'ID' attribute.";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         if (id.trim().toLowerCase().equals(tableID.toLowerCase())) {
            return tables[i];
         }
      }
      throw new NoSuchTableException("Couldn't find table with ID '" +
            tableID + "' in supplied catalog element.");
   }

   /** Returns the element describing the table with the given Name element.
    * Looks through all the elements so that it can check case insensitively.
    */
   protected static Element getTableElementByName(Element catalog, String tableName) throws MetadataException 
   {
      initialize(false);
      if (tableName == null || tableName.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (catalog == null) { 
         throw new IllegalArgumentException("Null catalog element supplied"); 
      }
      
      Element[] tables = DomHelper.getChildrenByTagName(catalog, "Table");
      for (int i = 0; i < tables.length; i++) {
         String name = DomHelper.getValueOf(tables[i], "Name");
         if ((name == null) || (name.trim().length()==0)) { 
           // NB: Schema validation should catch this error earlier
           String errorMsg = 
              "Metadoc is invalid, at least one table has a " +
              "missing/empty 'Name' element.";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         if (name.trim().toLowerCase().equals(tableName.toLowerCase())) {
            return tables[i];
         }
      }
      throw new NoSuchTableException("Couldn't find table with Name '" +
            tableName + "' in supplied catalog element.");
   }

   /** Return column info with the given id in the given table id.  
    * Ignores the catalog for now */
   protected static Element getColumnElementByID(String catalogID, String tableID, String columnID) throws MetadataException  
   {
      initialize(false);
      
      // KONA TOFIX : CatalogID currently allowed to be null, change later
      if (tableID == null || tableID.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (columnID == null || columnID.trim().length() == 0) { 
         throw new IllegalArgumentException("No column specified"); 
      }

      Element tableRes = getTableElementByID(
            getCatalogElementByID(catalogID), tableID);

      Element[] cols = DomHelper.getChildrenByTagName(tableRes, "Column");
      
      if (cols == null) {
         //no columns in given table
         throw new IllegalArgumentException("No Columns found in table "+tableID);
      }
      for (int i = 0; i < cols.length; i++) {
         // Allow for column IDs of the form "table.column" or 
         // "catalog.table.column" in the metadoc
         /*
         String colID = "";
         String fullID = cols[i].getAttribute("ID");
         int lastdot = fullID.lastIndexOf(".");
         if (lastdot == -1) { // No dot
            colID = fullID;
         }
         else {
            colID = fullID.substring(lastdot+1);
         }
         */
         //String colID = cols[i].getAttribute("ID");
         String colID = cols[i].getAttribute("ID");
         if (colID.trim().toLowerCase().equals(columnID.toLowerCase())) {
            return cols[i];
         }
      }
      throw new NoSuchColumnException("Couldn't find column with ID '" +
            columnID + "' in specified catalog and table.");
   }

   /** Return column info with the given Name in the given table Name.  
    * Ignores the catalog for now */
   protected static Element getColumnElementByName(String catalogName, String tableName, String columnName) throws MetadataException  
   {
      initialize(false);
      
      // KONA TOFIX : CatalogName currently allowed to be null
      //
      if (tableName == null || tableName.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (columnName == null || columnName.trim().length() == 0) { 
         throw new IllegalArgumentException("No column specified"); 
      }

      Element tableRes = getTableElementByName(
            getCatalogElementByName(catalogName), tableName);

      Element[] cols = DomHelper.getChildrenByTagName(tableRes, "Column");
      
      if (cols == null) {
         //no columns in given table
         throw new IllegalArgumentException("No Columns found in table "+tableName);
      }
      for (int i = 0; i < cols.length; i++) {
         String colName = DomHelper.getValueOf(
               DomHelper.getSingleChildByTagName(cols[i], "Name"));
         if (colName.trim().toLowerCase().equals(columnName.toLowerCase())) {
            return cols[i];
         }
      }
      throw new NoSuchColumnException("Couldn't find column with name '" +
            columnName + "' in specified catalog and table.");
   }
   

   

   /** Creates a TableInfo instance from the given RDBMS Resource table element */
   protected static TableInfo makeTableInfo(Element element) throws MetadataException 
   {
      initialize(false);
      
      if (element == null) { 
         throw new IllegalArgumentException("Received empty table element when trying to make TableInfo description"); 
      }
      
      TableInfo info = new TableInfo();
      info.setName(DomHelper.getValueOf(element, "Name"));
      String id = element.getAttribute("ID");
      info.setId(id);
      info.setDescription(nullIfEmpty(DomHelper.getValueOf(element, "Description")));
      // Now include conesearch information
      Element cone = DomHelper.getSingleChildByTagName(element, "ConeSettings");
      if (cone != null) {
         String raColName = DomHelper.getValueOf(cone,"RAColName").trim();
         String decColName = DomHelper.getValueOf(cone,"DecColName").trim();
         info.setConesearchable(true);
         info.setConeRAColName(raColName);
         info.setConeDecColName(decColName);
      }
      else {
         info.setConesearchable(false);
      }
      return info;
   }
   
   /** Creates a ColumnInfo instance from the given RDBMS Resource column element */
   protected static ColumnInfo makeColumnInfo(String catalogID, String tableID, Element element) throws MetadataException 
   {
      initialize(false);
      if (element == null) { 
         throw new IllegalArgumentException("Received empty column element when trying to make ColumnInfo description"); 
      }
      ColumnInfo info = new ColumnInfo();
      info.setName(DomHelper.getValueOf(element, "Name"));
      info.setGroup(getTableNameForID(catalogID, tableID), tableID);

      String id = element.getAttribute("ID");
      /*  DON'T DO THIS!
      if ((id == null) || (id.length()==0)) {
         id = info.getGroup()+"."+info.getName();
      }
      */
      info.setId(id);
      info.setDescription(nullIfEmpty(DomHelper.getValueOf(element, "Description")));
      info.setUnits(nullIfEmpty(DomHelper.getValueOf(element, "Units")));
      info.setErrorField(nullIfEmpty(DomHelper.getValueOf(element, "ErrorColumn")));

      Element[] ucdNodes = DomHelper.getChildrenByTagName(element, "UCD");
      for (int u = 0; u < ucdNodes.length; u++) {
         info.setUcd(DomHelper.getValueOf(ucdNodes[u]), ucdNodes[u].getAttribute("version"));
      }

      String datatype = nullIfEmpty(DomHelper.getValueOf(element, "Datatype"));
      if (datatype == null) {
         throw new MetadataException("Column "+info.getName()+" has no Datatype element in metadoc");
      }
      
      info.setPublicType(datatype);
      if (info.getPublicType() != null) {
         //info.setJavaType(XmlTypes.getJavaType(info.getPublicType()));
         info.setJavaType(StdDataTypes.getJavaType(info.getPublicType()));
      }
      log.debug("In metadoc interpreter: setting type of " + info.getName() + 
            " to protected type " + info.getPublicType().toString());
      return info;
   }
   
   /** Returns null if given string is empty/whitespace, or the string otherwise.
    * This makes it easier to check for unknown properties, such as UCDs, where you can
    * just check for nulls */
   protected static String nullIfEmpty(String s) {
      if (s.trim().length()==0) {
         return null;
      }
      return s;
   }


   /** Loads metadoc from given URL */
   private static void loadUrl(URL url) throws IOException {
      try {
         log.debug("Loading metadoc from "+url);
         metadoc = DomHelper.newDocument(url).getDocumentElement();
      }
      catch (SAXException e) {
         throw new MetadataException("Server's TableMetaDoc at "+url+" is invalid XML: "+e);
      }
      // We have loaded it, so it's syntactically valid at least.
      // Now check that it is the minimum required version - currently
      // version 1.1.
      String nodeName = metadoc.getNodeName();
      if ("DataDescription".equals(nodeName) || 
          "DatasetDescription".equals(nodeName)) {
         //String attrVal = metadoc.getAttribute("xmlns");
         String attrVal = metadoc.getNamespaceURI();
         if (attrVal == null || (attrVal.equals("")) ) { 
            // No namespace specified
            String errMess = 
               "Couldn't find default namespace in resource metadoc file; " +
               "please ensure it specifies the default 'xmlns' attribute.";
            log.error(errMess);
            throw new MetadataException(errMess);
         }
         else {
            boolean allowed = false;
            // look for allowed versions
            for (int i = 0; i < ALLOWED_METADOC_NAMESPACES.length; i++) {
               if (attrVal.equals(ALLOWED_METADOC_NAMESPACES[i])) {
                  allowed = true;
               }
            }
            if (allowed == false) {
               String errMess = 
                 "Resource metadoc file uses outdated/unknown metadoc schema '"+                  attrVal + 
                 "'; please update your metadoc to the latest version (" +
                  METADOC_NAMESPACE_LATEST + ")";
               log.error(errMess);
               throw new MetadataException(errMess);
            }
         }
      }
      // Now validate it against its schema
      // This will ensure that all compulsory attributes (e.g. the ID 
      // attribute) and tags (e.g. the Name tag) are present.
      String rootElement = metadoc.getLocalName();
      if(rootElement == null) {
         rootElement = metadoc.getNodeName();
      }
      try {
         AstrogridAssert.assertSchemaValid(metadoc,rootElement,SchemaMap.ALL);
      }
      catch (Throwable th) {
         throw new MetadataException("Resource metadoc file is invalid: "+th.getMessage(), th);
      }
      // Now perform some extra checks
      // We only do this once, when initially loading the metadoc, so we're
      // more concerned with thoroughness and legibility than efficiency here
      // - Check catalog names are case-insensitively unique
      // - Check table names are case-insensitively unique within catalogs
      // - Check column names are case-insensitively unique within tables
      // - Check Name tag and ID attribute values are not empty strings
      
      String errorMsg;
      String errS = "Metadoc is invalid: ";

      // Get all the catalogs and check their IDs and Names 
      Element[] catalogs = DomHelper.getChildrenByTagName(metadoc, "Catalog");

      // KONA REMOVE LATER
      // For now, restrict things to a single catalog (and assume JDBC
      // connection is restricted to that catalog);  later we need to
      // handle JDBC urls allowing access to multiple catalogs (schemas /
      // databases)
      String plugin = ConfigFactory.getCommonConfig().getString(
          "datacenter.querier.plugin");
      if (!plugin.equals(
          "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         if (catalogs.length > 1) {
            throw new MetadataException(
              "This release of DSA/catalog can publish only a single catalog;  please make sure your metadoc only contains one Catalog element.");
         }
      }
      // Check them
      checkIDsAndNames(catalogs, "Catalog");

      for (int i = 0; i < catalogs.length; i++) {

         // Get all tables for each catalog 
         Element[] tables = DomHelper.getChildrenByTagName(
               catalogs[i], "Table");
         // Now check that table IDs don't contain "." (don't think
         // old autogen metadoc made IDs of the form "catalog.table,
         // but just to be safe...)
         // NOTE: Catalog IDs may contain "." (sometimes necessary
         // for specifying schemas in SQL queries)
         for (int k = 0; k < tables.length; k++) {
            String theID = tables[k].getAttribute("ID");
            int lastdot = theID.lastIndexOf(".");
            if (lastdot != -1) { // Found a dot
               log.warn("Trimming prefix " + theID.substring(0,lastdot+1) +
                   "from table ID " +theID +
                   ": recommend removing prefix from your source metadoc"); 
               theID = theID.substring(lastdot+1);
               tables[k].setAttribute("ID",theID);
            }
         }
         // Now check their IDs and Names
         checkIDsAndNames(tables, "Table");

         for (int j = 0; j < tables.length; j++) {
            // Get all columns for each table 
            Element[] columns = DomHelper.getChildrenByTagName(
               tables[j], "Column");
            // Now check that columns don't contain "." (old autogen 
            // metadoc made IDs of the form "table.column", want to
            // eliminate these) - if they do, just trim off the 
            // prefix/prefices
            for (int k = 0; k < columns.length; k++) {
               String theID = columns[k].getAttribute("ID");
               int lastdot = theID.lastIndexOf(".");
               if (lastdot != -1) { // Found a dot
                  log.warn("Trimming prefix " + theID.substring(0,lastdot+1) +
                      "from column ID " +theID +
                      ": recommend removing prefix from your source metadoc"); 
                  theID = theID.substring(lastdot+1);
                  columns[k].setAttribute("ID",theID);
               }
            }
            // Now check their IDs and Names
            checkIDsAndNames(columns, "Column");

            // Now check for any conesearchable tables, and check that their
            // conesearch columns are sane.
            Element cone = DomHelper.getSingleChildByTagName(
               tables[j], "ConeSettings");
            if (cone != null) {
               String raColName = DomHelper.getValueOf(cone,"RAColName");
               if ((raColName == null) || ("".equals(raColName))) {
                  throw new MetadataException(
                    "Resource metadoc file is invalid: "+
                    "conesearch RaColName element may not be empty"); 
               }
               raColName = raColName.trim();
               String decColName = DomHelper.getValueOf(cone,"DecColName");
               if ((decColName == null) || ("".equals(decColName))) {
                  throw new MetadataException(
                    "Resource metadoc file is invalid: "+
                    "conesearch decColName element may not be empty"); 
               }
               decColName = decColName.trim();

               // Check columns are real columns
               boolean raFound = false;
               boolean decFound = false;
               String unitsRA = "";
               String unitsDec = "";
               for (int k = 0; k < columns.length; k++) {
                  String colName = DomHelper.getValueOf(columns[k], "Name");
                  if (colName == null) {
                     // Shouldn't get here!
                     throw new MetadataException(
                      "Resource metadoc file is invalid: "+
                      "column Name elements may not be empty or missing"); 
                  }
                  colName = colName.trim().toLowerCase();
                  if (raColName.toLowerCase().equals(colName)) {
                     raFound = true;
                     // Now check that units of column are recognized
                     unitsRA = DomHelper.getValueOf(columns[k], "Units");
                     if ("".equals(unitsRA)) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch RA column '" + colName +
                           "' must have Units with value \"deg\" or \"rad\"");
                     }
                     unitsRA = unitsRA.toLowerCase().trim();
                     if ( !unitsRA.equals("deg") && !unitsRA.equals("rad") ) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch RA column '" + colName +
                           "' must have Units with value \"deg\" or \"rad\"");
                     }
                  }
                  if (decColName.toLowerCase().equals(colName)) {
                     decFound = true;
                     // Now check that units of column are recognized
                     unitsDec = DomHelper.getValueOf(columns[k], "Units");
                     if ("".equals(unitsDec)) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch Dec column '" + colName +
                           "' must have Units of \"deg\" or \"rad\"");
                     }
                     unitsDec = unitsDec.toLowerCase().trim();
                     if ( !unitsDec.equals("deg") && !unitsDec.equals("rad") ) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch Dec column '" + colName +
                           "' must have Units with value \"deg\" or \"rad\"");
                     }
                  }
                  if (raFound && decFound) {
                     break;
                  }
               }
               if (!raFound) {
                  throw new MetadataException(
                    "Resource metadoc file is invalid: "+
                    "specified conesearch RaColName '"+ raColName + 
                    "' was not found in table " +
                        DomHelper.getValueOf(tables[j], "Name") );
               }
               if (!decFound) {
                  throw new MetadataException(
                    "Resource metadoc file is invalid: "+
                    "specified conesearch DecColName '"+ decColName + 
                    "' was not found in table " +
                        DomHelper.getValueOf(tables[j], "Name") );
               }
               if (!unitsRA.equals(unitsDec)) {
                   throw new MetadataException(
                      "Resource metadoc file is invalid: "+
                       "Conesearch RA and Dec columns must have the same units"); 
               }
            }
         }
      }
   }
   
   protected static void checkIDsAndNames(Element[] elements, 
         String elementType) throws MetadataException 
   {
      String errorMsg;
      String errS = "Metadoc is invalid: ";

      // Check elements aren't null
      if (elements == null || elements.length == 0) {
         errorMsg = errS + "Unexpected zero number of elements of type " + 
            elementType +  " found.";
         log.error(errorMsg);
         throw new MetadataException(errorMsg);
      }
      // First check that the names and IDs aren't null/empty,
      // and that names don't contain any "." characters
      for (int i = 0; i < elements.length; i++) {
         String theID = elements[i].getAttribute("ID");
         if ((theID == null || theID.trim().length() == 0)) {
            errorMsg = errS + "Null/empty " + elementType + 
                "ID attribute value present";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         String theName = DomHelper.getValueOf(elements[i], "Name");
         if ((theName == null || theName.trim().length() == 0)) {
            errorMsg = errS + "Null/empty " + elementType + 
               "Name tag value present";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         int dotIndex = theName.indexOf('.');
         if (dotIndex != -1) {   //Dot found (shouldn't get here)
            errorMsg = errS + "Element of type " + elementType + 
               "has illegal name '" + theName + 
               "': the '.' character is not allowed";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }

      }
      // Now check that the names and IDs are case-insensitively unique
      for (int i = 0; i < elements.length; i++) {
         String theID1 = elements[i].getAttribute("ID");
         String theName1 = DomHelper.getValueOf(elements[i], "Name");
         for (int j = i+1; j < elements.length; j++) {
            String theID2 = elements[j].getAttribute("ID");
            if (theID1.trim().toLowerCase().equals(
                     theID2.trim().toLowerCase())) {
               errorMsg = errS + "Duplicate " + elementType + " IDs '" +
                  theID1 + "' and '" + theID2 + 
                  "' present in same parent element";
               log.error(errorMsg);
               throw new MetadataException(errorMsg);
            }
            String theName2 = DomHelper.getValueOf(elements[j], "Name");
            if (theName1.trim().toLowerCase().equals(
                     theName2.trim().toLowerCase())) {
               errorMsg = errS + "Duplicate " + elementType + "Names '" +
                  theName1 + "' and '" + theName2 + 
                  "' present in same parent element";
               log.error(errorMsg);
               throw new MetadataException(errorMsg);
            }
         }
      }
   }
}


