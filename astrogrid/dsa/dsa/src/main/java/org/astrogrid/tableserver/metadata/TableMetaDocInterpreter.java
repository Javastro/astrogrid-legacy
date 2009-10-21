/*
 * $Id: TableMetaDocInterpreter.java,v 1.2 2009/10/21 19:01:00 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
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
   
   //protected static boolean initialized = false;

// Element[] catalogs; //root list of catalogs
   protected static Element metadoc = null;

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
      throw new MetadataException("Please don't instantiate the TableMetaDocInterpreter class - all its useful methods are static methods.");
   }

   public static void initialize() throws MetadataException
   {
      URL theUrl = null;
      try {
         theUrl = ConfigFactory.getCommonConfig().getUrl(
              TABLE_METADOC_URL_KEY, null);
         if (theUrl == null) {
            String fileName = ConfigFactory.getCommonConfig().getString(
                 TABLE_METADOC_FILE_KEY);
             theUrl = ConfigReader.resolveFilename(fileName);
         }
         if (theUrl == null) {
            throw new MetadataException("Could not resolve location of DSA metadoc file, please check your configuration!");
         }
      }
      catch (IOException ioe) {
         if (ioe instanceof FileNotFoundException) {
            throw new MetadataException("The specified metadoc file for this DSA cannot be found, please check your configuration.");
         }
         throw new MetadataException(ioe.getMessage());
      }
      initialize(theUrl);
   }

   /** Initialize to interpret the table metadoc given in the config file.
    * If "force" is set to true, always re-initializes the metadata. 
    * The guts of method must be synchronized on the class itself to 
    * ensure that the "metadoc" variable is accessed safely by multiple 
    * threads. */
   public static void initialize(URL metadocUrl) throws MetadataException {
     log.info("Loading table metadata from " + metadocUrl);
     synchronized (TableMetaDocInterpreter.class) {
        if (metadoc != null) { //Initialized already
          //log.error("KONA METADOC ALREADY LOADED!");
          return;
        }
        else { // metadoc is null
           try {
             // Initialise SampleStars plugin if required 
             // (may not be initialised if admin has not run the self-tests)
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
            try {
               //log.error("KONA LOADING METADOC!");
               metadoc = loadAndValidateMetadoc(metadocUrl);
               //log.error("KONA FINISHED LOADING METADOC!");
            }
            catch (FileNotFoundException e) {
              String message =
                  String.format("Can't find the metadoc file %s", metadocUrl);
              log.error(message);
              throw new MetadataException(message, e);
            }
            catch (IOException e) {
              String message =
                  String.format("Metadoc file %s is invalid", metadocUrl);
              log.error(message);
              log.error(e);
              log.error(e.getCause());
              throw new MetadataException(message, e);
            }
            // After initial syntactic validation, make sure our conesearch 
            // settings (if any) are ok
            try {
               checkValidConeTables();
            }
            catch (MetadataException me) {
               metadoc = null;  // If a problem, don't accept it
               throw me;
            }
        }// End of initialization actions
      }//End of synchronization block
   }

   /**
    * Clears a previously-loaded metadoc, allowing a new one to replace it.
    * This is only appropriate for unit testing.
    */
   public static void clear() {
     synchronized(TableMetaDocInterpreter.class) {
       metadoc = null;
     }
   }

   public static boolean isValid() {
      try {
         initialize();
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
      initialize();

      Element[] elements = getChildrenByTagName(metadoc, "Catalog");
      String[] catIDs = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catIDs[i] = getAttribute(elements[i],("ID"));
      }
      return catIDs;
   }

   /** Returns the list of names of all the catalogs in the metadoc */
   public static String[] getCatalogNames() throws MetadataException  
   {
      initialize();
      Element[] elements = getChildrenByTagName(metadoc, "Catalog");
      String[] catNames = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catNames[i] = getValueOf(elements[i], "Name");
      }
      return catNames;
   }

   /** Returns the list of descriptions of all the catalogs in the metadoc */
   public static String[] getCatalogDescriptions() throws MetadataException  
   {
      initialize();
      Element[] elements = getChildrenByTagName(metadoc, "Catalog");
      String[] catDescs = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catDescs[i] = getValueOf(elements[i], "Description");
      }
      return catDescs;
   }

   /** Return the Name associated with a particular catalog ID */
   public static String getCatalogNameForID(String catalogID) 
         throws MetadataException 
   {
      initialize();
      Element catNode = getCatalogElementByID(catalogID);
      return getValueOf(catNode, "Name");
   }
   /** Return the ID associated with a particular catalog Name */
   public static String getCatalogIDForName(String catalogName) 
         throws MetadataException 
   {
      initialize();
      Element catNode = getCatalogElementByName(catalogName);
      return getAttribute(catNode,"ID");
   }


   /** Returns all conesearchable tables.  */
   public static TableInfo[] getConesearchableTables() throws MetadataException 
   {
      initialize();
      Vector vectorOfInfos = new Vector();
      initialize();
      // Get all catalogs
      Element[] catalogs = getChildrenByTagName(metadoc, "Catalog");
      for (int i = 0; i < catalogs.length; i++) {
         // Get all tables
         Element[] tables = getChildrenByTagName(
              catalogs[i], "Table");
         for (int j = 0; j < tables.length; j++) {
            TableInfo info = makeTableInfo(tables[j]);
            if (info.getConesearchable() == true) {
               String catalogID = getAttribute(catalogs[i],"ID");
               info.setCatalog(getCatalogNameForID(catalogID),catalogID);
               vectorOfInfos.add(info);
            }
         }
      }
      TableInfo[] infoArray = new TableInfo[vectorOfInfos.size()];
      return (TableInfo[])vectorOfInfos.toArray(infoArray);
   }

   /** Returns all conesearchable tables in the specified catalog.  */
   public static TableInfo[] getConesearchableTables(String catalogID) 
      throws MetadataException 
   {
      initialize();
      Vector vectorOfInfos = new Vector();
      initialize();
      // Get all tables for the specified catalog IDcatalogs
      TableInfo[] tables = getTablesInfoByID(catalogID);
      for (int i = 0; i < tables.length; i++) {
         if (tables[i].getConesearchable() == true) {
            vectorOfInfos.add(tables[i]);
         }
      }
      TableInfo[] infoArray = new TableInfo[vectorOfInfos.size()];
      return (TableInfo[])vectorOfInfos.toArray(infoArray);
   }

   /** Return table info on the table with the given ID.  */
   public static TableInfo getTableInfoByID(String catalogID, String tableID) 
         throws MetadataException 
   {
      initialize();
      TableInfo info = makeTableInfo(getTableElementByID(
               getCatalogElementByID(catalogID), tableID));
      info.setCatalog(getCatalogNameForID(catalogID), catalogID);
      return info;
   }

   /** Return table info on the table with the given Name*/
   public static TableInfo getTableInfoByName(String catalogName, 
         String tableName) throws MetadataException 
   {
      initialize();
      TableInfo info = makeTableInfo(getTableElementByName(
               getCatalogElementByName(catalogName), tableName));
      info.setCatalog(catalogName, getCatalogIDForName(catalogName));
      return info;
   }

   /** Return the Name associated with a particular table ID */
   public static String getTableNameForID(String catalogID, String tableID) 
       throws MetadataException
   {
      initialize();
      Element catNode = getCatalogElementByID(catalogID);
      Element tableNode = getTableElementByID(catNode, tableID);
      return getValueOf(tableNode, "Name");
   }
   /** Return the ID associated with a particular table Name */
   public static String getTableIDForName(String catalogName, String tableName)
         throws MetadataException 
   {
      initialize();
      Element catNode = getCatalogElementByName(catalogName);
      Element tableNode = getTableElementByName(catNode, tableName);
      return getAttribute(tableNode,"ID");
   }

   /** Return the ID associated with a particular table Name, when
    * we don't know the name of the parent catalog.
    * KONA TOFIX: hopefully this method can be deprecated once the 
    * ADQL queries are embedding catalog as well as table names. */
   public static String guessTableIDForName(String tableName)
         throws MetadataException 
   {
      initialize();
      String foundID = null;  // Don't know what the ID is
      Element[] cats = getChildrenByTagName(metadoc, "Catalog");
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
            foundID = getAttribute(tableNode,"ID");
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
      initialize();
      Element[] elements = getChildrenByTagName(getCatalogElementByID(catalogID), "Table");
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
      initialize();
      String catID = getCatalogIDForName(catalogName);
      return getTablesInfoByID(catID);
   }


   /** Return column info for the column with the given ID in the given 
    * table and catalog IDs */
   public static ColumnInfo getColumnInfoByID(String catalogID, String tableID, 
         String columnID) throws MetadataException 
   {
      initialize();
      return makeColumnInfo(catalogID,
            tableID, getColumnElementByID(catalogID, tableID, columnID));
   }

   /** Return column info for the column with the given Name in the given 
    * table and catalog Names */
   public static ColumnInfo getColumnInfoByName(String catalogName, 
         String tableName, String columnName) throws MetadataException 
   {
      initialize();
      // Translate to IDs, needed by makeColumnInfo
      String catID = getCatalogIDForName(catalogName);
      String tableID = getTableIDForName(catalogName,tableName);
      return makeColumnInfo(catID,
          tableID, getColumnElementByName(catalogName, tableName, columnName));
   }

   /** Return the ID associated with a particular column Name */
   public static String getColumnIDForName(String catalogName, String tableName,          String colName) throws MetadataException 
   {
      initialize();
      Element colNode = getColumnElementByName(catalogName, tableName, colName);
      return getAttribute(colNode,"ID");
   }
   /** Return the Name associated with a particular column ID */
   public static String getColumnNameForID(String catalogID, String tableID, 
         String colID) throws MetadataException {
      initialize();
      Element colNode = getColumnElementByID(catalogID, tableID, colID);
      return getValueOf(colNode, "Name");
   }


   /** Returns the RA column for a particular conesearchable table */
   public static String getConeRAColumnByName(String catalogName, String tableName)
            throws MetadataException {

      initialize();
      Element tableNode = getTableElementByName(
            getCatalogElementByName(catalogName), tableName);
      Element cone = getSingleChildByTagName(tableNode, "ConeSettings");
      if (cone == null) {
         // Table is not conesearchable!!
         throw new MetadataException("Table '" + tableName + 
               "' in catalog '" + catalogName + 
               "' is not configured for conesearch");
      }
      // Should be valid, checked at load-time
      return (getValueOf(cone,"RAColName").trim());
   }
   /** Returns the Dec column for a particular conesearchable table */
   public static String getConeDecColumnByName(
         String catalogName, String tableName) throws MetadataException 
   {
      initialize();
      Element tableNode = getTableElementByName(
            getCatalogElementByName(catalogName), tableName);
      Element cone = getSingleChildByTagName(
            tableNode, "ConeSettings");
      if (cone == null) {
         // Table is not conesearchable!!
         throw new MetadataException("Table '" + tableName + 
               "' in catalog '" + catalogName + 
               "' is not configured for conesearch");
      }
      // Should be valid, checked at load-time
      return (getValueOf(cone,"DecColName").trim());
   }
   /** Returns the Units for a particular column, sanitychecked to make
    * sure that they are valid conesearch units (deg or rad) */
   public static String getConeUnitsByName(String catalogName, String tableName)
         throws MetadataException 
   {
      initialize();
      String raCol = getConeRAColumnByName(catalogName, tableName);
      Element colNode = getColumnElementByName(catalogName, tableName, raCol);
      String units = getValueOf(colNode,"Units");
      /*
      if ( 
            !"deg".equals(units) && !"rad".equals(units) &&
            !"degrees".equals(units) && !"radians".equals(units) 
      ) {
         //Shouldn't get here, has been prechecked at load time
         throw new MetadataException("Column '" + raCol + "' in table '" + 
               tableName + "' in catalog '" + catalogName + 
               "' does not have expected units of 'deg' or 'rad' - " +
               "found '" +units+"' instead.'");
      }
      */
      if ("degrees".equals(units)) {
         return "deg";
      }   
      else if ("radians".equals(units)) {
         return "rad";
      }
      return units;
   }


   /** Creates an array of ColumnInfo instances representing the columns
    * in the specified table, selected by catalog and table ID.
    */
   public static ColumnInfo[] getColumnsInfoByID(String catalogID, 
         String tableID) throws MetadataException 
   {
      initialize();
      Element tableElement = 
            getTableElementByID(getCatalogElementByID(catalogID), tableID);
      Element[] elements = 
            getChildrenByTagName(tableElement, "Column");
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
      initialize();
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
      initialize();

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
                getChildrenByTagName(catNode, "Table");
            localTables = new String[realTables.length];
            for (int i = 0; i < realTables.length; i++) {
               localTables[i] = getValueOf(getSingleChildByTagName(realTables[i], "Name"));
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
                  Element[] cols = getChildrenByTagName(
                        tableElement, "Column");
                  for (int c = 0; c < cols.length; c++) {
                     String colID = getAttribute(cols[c],"ID");
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
      initialize();
      Element[] cats = getChildrenByTagName(metadoc, "Catalog");
      if (cats.length > 0) {
         log.debug("Requested null catalog name/ID : defaulting to first catalog specified in metadoc, with ID " + getAttribute(cats[0],"ID"));
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
      initialize();
      if ((catalogID == null) || (catalogID.trim().length()==0)) {
         return getCatalogForNullParam();
      }
      Element[] cats = getChildrenByTagName(metadoc, "Catalog");

      for (int i = 0; i < cats.length; i++) {
         String catId = getAttribute(cats[i],"ID");
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
      initialize();
      if ((catalogName == null) || (catalogName.trim().length()==0)) {
         return getCatalogForNullParam();
      }
      Element[] cats = getChildrenByTagName(metadoc, "Catalog");
      for (int i = 0; i < cats.length; i++) {
         String catName = getValueOf(cats[i], "Name");
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
      initialize();
      if (tableID == null || tableID.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (catalog == null) { 
         throw new IllegalArgumentException("Null catalog element supplied"); 
      }
      Element[] tables = getChildrenByTagName(catalog, "Table");
      for (int i = 0; i < tables.length; i++) {
         String id = getAttribute(tables[i],"ID");
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
            tableID + "' in supplied catalog element with ID '" + 
            getAttribute(catalog,"ID") + "'");
   }

   /** Returns the element describing the table with the given Name element.
    * Looks through all the elements so that it can check case insensitively.
    */
   protected static Element getTableElementByName(Element catalog, String tableName) throws MetadataException 
   {
      initialize();
      if (tableName == null || tableName.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (catalog == null) { 
         throw new IllegalArgumentException("Null catalog element supplied"); 
      }
      
      Element[] tables = getChildrenByTagName(catalog, "Table");
      for (int i = 0; i < tables.length; i++) {
         String name = getValueOf(tables[i], "Name");
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
            tableName + "' in supplied catalog element with Name '" +
            getValueOf(catalog, "Name") + "'");
   }

   /** Return column info with the given id in the given table id.  
    * Ignores the catalog for now */
   protected static Element getColumnElementByID(String catalogID, String tableID, String columnID) throws MetadataException  
   {
      initialize();
      
      // KONA TOFIX : CatalogID currently allowed to be null, change later
      if (tableID == null || tableID.trim().length() == 0) { 
         throw new IllegalArgumentException("No table specified"); 
      }
      if (columnID == null || columnID.trim().length() == 0) { 
         throw new IllegalArgumentException("No column specified"); 
      }

      Element tableRes = getTableElementByID(
            getCatalogElementByID(catalogID), tableID);

      Element[] cols = getChildrenByTagName(tableRes, "Column");
      
      if (cols == null) {
         //no columns in given table
         throw new IllegalArgumentException("No Columns found in table "+tableID);
      }
      for (int i = 0; i < cols.length; i++) {
         // Allow for column IDs of the form "table.column" or 
         // "catalog.table.column" in the metadoc
         /*
         String colID = "";
         String fullID = getAttribute(cols[i],"ID");
         int lastdot = fullID.lastIndexOf(".");
         if (lastdot == -1) { // No dot
            colID = fullID;
         }
         else {
            colID = fullID.substring(lastdot+1);
         }
         */
         //String colID = getAttribute(cols[i],"ID");
         String colID = getAttribute(cols[i],"ID");
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
      initialize();
      
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

      Element[] cols = getChildrenByTagName(tableRes, "Column");
      
      if (cols == null) {
         //no columns in given table
         throw new IllegalArgumentException("No Columns found in table "+tableName);
      }
      for (int i = 0; i < cols.length; i++) {
         String colName = getValueOf(
               getSingleChildByTagName(cols[i], "Name"));
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
      initialize();
      
      if (element == null) { 
         throw new IllegalArgumentException("Received empty table element when trying to make TableInfo description"); 
      }
      
      TableInfo info = new TableInfo();
      info.setName(getValueOf(element, "Name"));
      String id = getAttribute(element,"ID");
      info.setId(id);
      info.setDescription(nullIfEmpty(getValueOf(element, "Description")));
      // Now include conesearch information
      Element cone = getSingleChildByTagName(element, "ConeSettings");
      if (cone != null) {
         String raColName = getValueOf(cone,"RAColName").trim();
         String decColName = getValueOf(cone,"DecColName").trim();
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
      initialize();
      if (element == null) { 
         throw new IllegalArgumentException("Received empty column element when trying to make ColumnInfo description"); 
      }
      ColumnInfo info = new ColumnInfo();
      info.setName(getValueOf(element, "Name"));
      info.setGroup(getTableNameForID(catalogID, tableID), tableID);
      info.setParent(getCatalogNameForID(catalogID), catalogID);

      String id = getAttribute(element,"ID");
      /*  DON'T DO THIS!
      if ((id == null) || (id.length()==0)) {
         id = info.getGroup()+"."+info.getName();
      }
      */
      info.setId(id);
      info.setDescription(nullIfEmpty(getValueOf(element, "Description")));
      info.setUnits(nullIfEmpty(getValueOf(element, "Units")));
      info.setErrorField(nullIfEmpty(getValueOf(element, "ErrorColumn")));

      Element[] ucdNodes = getChildrenByTagName(element, "UCD");
      for (int u = 0; u < ucdNodes.length; u++) {
         info.setUcd(getValueOf(ucdNodes[u]), getAttribute(ucdNodes[u],"version"));
      }

      String datatype = nullIfEmpty(getValueOf(element, "Datatype"));
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

   /* This one is for unit tests */
   /*
   public static void validateFileMetadoc(String filename) throws IOException {
      MetadocInterpreterTest.class.getResource(
                           metadocFilename).toString());

      URL theUrl = ConfigReader.resolveFilename(filename);
      //System.out.println("KONA THE URL IS " + theUrl.toString());
      Element metadoc = loadAndValidateMetadoc(theUrl);
   }
   */


   /** Loads metadoc from given URL and validates it.  
    * This one doesn't need to be synchronised because it accesses its
    * own private Element.
    */
   public static Element loadAndValidateMetadoc(URL url) throws IOException {
      Element urlElement = null;
      try {
         log.debug("Loading metadoc from "+url);
         urlElement = DomHelper.newDocument(url).getDocumentElement();
      }
      catch (SAXException e) {
         throw new MetadataException("Server's TableMetaDoc at "+url+" is invalid XML: "+e);
      }
      // We have loaded it, so it's syntactically valid at least.
      // Now check that it is the minimum required version - currently
      // version 1.1.
      String nodeName = urlElement.getNodeName();
      if ("DataDescription".equals(nodeName) || 
          "DatasetDescription".equals(nodeName)) {
         //String attrVal = urlElement.getAttribute("xmlns");
         String attrVal = urlElement.getNamespaceURI();
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
      String rootElement = urlElement.getLocalName();
      if(rootElement == null) {
         rootElement = urlElement.getNodeName();
      }
      try {
         AstrogridAssert.assertSchemaValid(urlElement,rootElement,SchemaMap.ALL);
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
      Element[] catalogs = getChildrenByTagName(urlElement, "Catalog");

      // KONA REMOVE LATER
      // For now, restrict things to a single catalog (and assume JDBC
      // connection is restricted to that catalog);  later we need to
      // handle JDBC urls allowing access to multiple catalogs (schemas /
      // databases)
      String plugin = ConfigFactory.getCommonConfig().getString(
          "datacenter.querier.plugin");
      /*
      if (!plugin.equals(
          "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         if (catalogs.length > 1) {
            throw new MetadataException(
              "This release of DSA/catalog can publish only a single catalog;  please make sure your metadoc only contains one Catalog element.");
         }
      }
      */
      // Check them
      checkIDsAndNames(catalogs, "Catalog");

      for (int i = 0; i < catalogs.length; i++) {

         // Get all tables for each catalog 
         Element[] tables = getChildrenByTagName(
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
            Element[] columns = getChildrenByTagName(
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
            Element cone = getSingleChildByTagName(
               tables[j], "ConeSettings");
            if (cone != null) {
               String raColName = getValueOf(cone,"RAColName");
               if ((raColName == null) || ("".equals(raColName))) {
                  throw new MetadataException(
                    "Resource metadoc file is invalid: "+
                    "conesearch RaColName element may not be empty"); 
               }
               raColName = raColName.trim();
               String decColName = getValueOf(cone,"DecColName");
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
                  String colName = getValueOf(columns[k], "Name");
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
                     unitsRA = getValueOf(columns[k], "Units");
                     if ("".equals(unitsRA)) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch RA column '" + colName +
                           "' must have Units with value \"deg\" or \"rad\"");
                     }
                     unitsRA = unitsRA.toLowerCase().trim();
                     if ( 
                       !unitsRA.equals("deg") && !unitsRA.equals("rad") &&
                       !unitsRA.equals("degrees") && !unitsRA.equals("radians") 
                     ) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch RA column '" + colName +
                           "' must have Units with value \"deg\" or \"rad\"");
                     }
                  }
                  if (decColName.toLowerCase().equals(colName)) {
                     decFound = true;
                     // Now check that units of column are recognized
                     unitsDec = getValueOf(columns[k], "Units");
                     if ("".equals(unitsDec)) {
                        throw new MetadataException(
                          "Resource metadoc file is invalid: "+
                           "Conesearch Dec column '" + colName +
                           "' must have Units of \"deg\" or \"rad\"");
                     }
                     unitsDec = unitsDec.toLowerCase().trim();
                     if ( 
                       !unitsDec.equals("deg") && !unitsDec.equals("rad") &&
                       !unitsDec.equals("degrees") && !unitsDec.equals("radians") 
                     ) {
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
                        getValueOf(tables[j], "Name") );
               }
               if (!decFound) {
                  throw new MetadataException(
                    "Resource metadoc file is invalid: "+
                    "specified conesearch DecColName '"+ decColName + 
                    "' was not found in table " +
                        getValueOf(tables[j], "Name") );
               }
               if (!unitsRA.equals(unitsDec)) {
                   throw new MetadataException(
                      "Resource metadoc file is invalid: "+
                       "Conesearch RA and Dec columns must have the same units"); 
               }
            }
         }
      }
      return urlElement;
   }

   /** Checks to see that the conesearchable tables have the expected
    * UCDs to produce valid conesearch output. */

   protected static void checkValidConeTables() throws MetadataException
   {
      //Now check that conesearchable tables have the proper UCDs
      TableInfo[] tables = getConesearchableTables();
      for (int i = 0; i < tables.length; i++) {
         String catalogName = tables[i].getCatalogName();
         String tableName = tables[i].getName();

         // Check RA column has valid UCD
         String raColName = getConeRAColumnByName(catalogName, tableName);
         ColumnInfo raColInfo = getColumnInfoByName(
               catalogName, tableName, raColName);
         String raUcd = raColInfo.getUcd("1");
         if (
           (raUcd == null) ||
           //Bit of a kludge, really ought to parse the UCD properly
           (raUcd.indexOf("POS_EQ_RA_MAIN") == -1)
         ) {
            throw new MetadataException(
               "Resource metadoc file is invalid: "+
               "Conesearchable RA column with name '" +raColName +
               "' in table with name '" + tableName +
               "' in catalog with name '" + catalogName +
               "' must have its <UCD version='1'> tag set to 'POS_EQ_RA_MAIN' "+
               "(in order to produce valid conesearch results.) "); }

         // Check Dec column has valid UCD
         String decColName = getConeDecColumnByName(catalogName, tableName);
         ColumnInfo decColInfo = getColumnInfoByName(
               catalogName, tableName, decColName);
         String decUcd = decColInfo.getUcd("1");
         if (
           (decUcd == null) ||
           //Bit of a kludge, really ought to parse the UCD properly
           (decUcd.indexOf("POS_EQ_DEC_MAIN") == -1)
         ) {
            throw new MetadataException(
               "Resource metadoc file is invalid: "+
               "Conesearchable Dec column with name '" +decColName +
               "' in table with name '" + tableName +
               "' in catalog with name '" + catalogName +
               "' must have its <UCD version='1'> tag set to 'POS_EQ_DEC_MAIN' "+
               "(in order to produce valid conesearch results.) ");
         }
         // Check ID_MAIN column exists
         ColumnInfo[] allCols = getColumnsInfoByName(catalogName, tableName);
         int found = 0;
         for (int j = 0; j < allCols.length; j++) {
            String ucd = allCols[j].getUcd("1");
            if (ucd != null) {
               StringTokenizer tokenizer = new StringTokenizer(ucd,";");
               while (tokenizer.hasMoreTokens()) {
                  String token = tokenizer.nextToken().trim();
                  if ("ID_MAIN".equals(token)) {
                     found = found + 1;
                  }
               }
            }
         }
         if (found != 1) {
            throw new MetadataException(
               "Resource metadoc file is invalid: "+
               "' Conesearchable table with name '" + tableName +
               "' in catalog with name '" + catalogName +
               "' must contain exactly one column with the " +
               "' <UCD version='1'> tag set to 'ID_MAIN' " +
               "(in order to produce valid conesearch results.) ");
         }
      }
   }

   /** Checks to see that a string contains only characters allowed
     in published names. */
   protected static String checkValidName(String name) throws MetadataException
   {
      if (name == null || "".equals(name)) {
         return("Name fields in the DSA metadoc may not be left empty");
      }
      if (!Character.isLetter(name.charAt(0))) {
         return("Name fields in the DSA metadoc MUST begin with a letter - name '" + name +"' is illegal");
      }
      for (int i = 1; i < name.length(); i++) {
        if (!(
         Character.isLetter(name.charAt(i)) ||
         Character.isDigit(name.charAt(i)) ||
         name.charAt(i) == '_'
        )) {
         return("Name fields in the DSA metadoc MUST contain only letters, numbers or underscores - name '" + name +"' is illegal");
        }
      }
      // If we got here, all ok
      return null;
   }

   /** This is only used by loadAndValidateMetadoc and doesn't 
    * need extra synchronization */
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
      // and that names don't contain any illegal characters
      for (int i = 0; i < elements.length; i++) {
         String theID = elements[i].getAttribute("ID");
         if ((theID == null || theID.trim().length() == 0)) {
            errorMsg = errS + "Null/empty " + elementType + 
                "ID attribute value present";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         String theName = getValueOf(elements[i], "Name");
         if ((theName == null || theName.trim().length() == 0)) {
            errorMsg = errS + "Null/empty " + elementType + 
               "Name tag value present";
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
         String nameOk = checkValidName(theName);
         if (nameOk != null) {
            errorMsg = errS + "Element of type " + elementType + 
               " has illegal name '" + theName + 
               "': " + nameOk;
            log.error(errorMsg);
            throw new MetadataException(errorMsg);
         }
      }
      // Now check that the names and IDs are case-insensitively unique
      for (int i = 0; i < elements.length; i++) {
         String theID1 = elements[i].getAttribute("ID");
         String theName1 = getValueOf(elements[i], "Name");
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
            String theName2 = getValueOf(elements[j], "Name");
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
   private static String getAttribute(Element element, String attName) {
      synchronized (TableMetaDocInterpreter.class) {
         return element.getAttribute(attName);
      }
   }
   private static Element getSingleChildByTagName(Element element, String tagName) {
      synchronized (TableMetaDocInterpreter.class) {
         return DomHelper.getSingleChildByTagName(element, tagName);
      }
   }
   private static String getValueOf(Element element, String tagName) {
      synchronized (TableMetaDocInterpreter.class) {
         return DomHelper.getValueOf(element, tagName);
      }
   }
   private static String getValueOf(Element element) {
      synchronized (TableMetaDocInterpreter.class) {
         return DomHelper.getValueOf(element);
      }
   }

   private static Element[] getChildrenByTagName(Element parent, String tagName) {
      synchronized (TableMetaDocInterpreter.class) {
         return DomHelper.getChildrenByTagName(parent, tagName);
      }
   }
}
