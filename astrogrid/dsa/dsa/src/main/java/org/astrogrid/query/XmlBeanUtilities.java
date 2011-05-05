/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import org.astrogrid.cfg.ConfigFactory;

// XMLBeans stuff
import org.apache.xmlbeans.* ;

// For validation of beans
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.astrogrid.adql.beans.ArrayOfFromTableType;
import org.astrogrid.adql.beans.ColumnReferenceType;
import org.astrogrid.adql.beans.FromTableType;
import org.astrogrid.adql.beans.FromType;
import org.astrogrid.adql.beans.JoinTableType;
import org.astrogrid.adql.beans.SelectDocument;
import org.astrogrid.adql.beans.SelectType;
import org.astrogrid.adql.beans.SelectionItemType;
import org.astrogrid.adql.beans.SelectionLimitType;
import org.astrogrid.adql.beans.SelectionListType;
import org.astrogrid.adql.beans.TableType;




/**
 * Utility clas for manipulating XMLBeans structures.
 * @author K Andrews
 *
 */

public class XmlBeanUtilities  {

   /** For xmlbeans validation against schema */
   private static XmlOptions xmlOptions;

   /** Returns the XML query as a string */
   public static String getAdqlString(SelectDocument selectDocument) 
        throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      return selectDocument.toString();
   }

   /** Returns the XML query as a string suitable for embedding in HTML */
   public static String getHtmlAdqlString(SelectDocument selectDocument) 
         throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      String xmlString = selectDocument.toString();
      //  Make HTML-display-friendly
      xmlString = xmlString.replaceAll(">", "&gt;");
      xmlString = xmlString.replaceAll("<", "&lt;");
      xmlString = xmlString.replaceAll("\n","<br/>");
      return xmlString;
   }
   
   /**
    * Returns maximum number of results specified in the actual query */
   public static long getLimit(SelectDocument selectDocument) throws QueryException
   {      
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetRestrict()) {
         SelectionLimitType restrict = selectType.getRestrict();
         if (restrict.isSetTop()) {
            return restrict.getTop();
         }
      }
      return Query.LIMIT_NOLIMIT;
   }

   
   /** Returns the lowest of the query limit (stored in the selectDocument) 
    *  or local limit (configured in DSA setup) */
   public static long getLocalLimit(SelectDocument selectDocument) throws QueryException 
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      long queryLimit = Query.LIMIT_NOLIMIT;  
      long localLimit = ConfigFactory.getCommonConfig().getInt(
            Query.MAX_RETURN_KEY, Query.LIMIT_NOLIMIT);
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetRestrict()) {
         SelectionLimitType restrict = selectType.getRestrict();
         if (restrict.isSetTop()) {
            queryLimit = restrict.getTop();
         }
      }
      if ((queryLimit == Query.LIMIT_NOLIMIT) || 
             ((queryLimit > localLimit) && (localLimit > 0))) {
         queryLimit = localLimit;
      }
      return queryLimit;
   }

   /** Sets the row limit (including removing it altogether).
    * We should not allow the row limit to be publicly manipulated -
    * this is just for temporarily tweaking the query to reflect the
    * datacenter row limit (if necessary) when converting to SQL.
    */
   public static void setLimit(SelectDocument selectDocument, long limit) throws QueryException
   {      
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      SelectType selectType = selectDocument.getSelect();
      if (limit == Query.LIMIT_NOLIMIT) { // Remove limit clause altogether
         if (selectType.isSetRestrict()) {
            // Already have a restrict clause - remove it
            selectType.unsetRestrict();
         }
      }
      else {   // Limit is a fixed value
         if (!selectType.isSetRestrict()) {
            selectType.addNewRestrict(); // No Restrict set, so create one
         }
         selectType.getRestrict().setTop(limit);
      }
   }
   
   /** Returns all catalog names for all tables that are used in the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public static String[] getParentCatalogReferences(
         SelectDocument selectDocument, boolean uniqueOnly) 
          throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      // Need to find all From Tables with type tableType
      // and extract the Name attributes 
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         TableType[] tables = getAllBaseTables(from);
         int numTables = tables.length;
         Vector parentNames = new Vector();
         for (int i = 0; i < numTables; i++) {
            String name = "";
            String parentCatalog = null;     //Default
            TableType tableType = tables[i];
            name = tableType.getName();
            parentCatalog = getParentCatalog(tableType);
            boolean duplicate = false;
            for (int j = 0; j < parentNames.size(); j++) {
               if (name.equals((String)(parentNames.elementAt(j)))) {
                  duplicate = true;
                  break;
               }
            }
            if ((!duplicate) || (!uniqueOnly)) {
               parentNames.addElement(parentCatalog);
            }    
         }
         numTables = parentNames.size();
         String[] parents = new String[numTables];
         for (int i = 0; i < numTables; i++) {
           parents[i] = (String)parentNames.elementAt(i);
         }
         return parents;
      }
      return new String[0];
   } 

   /** Returns all table names that are used in the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public static String[] getTableReferences(SelectDocument selectDocument,
         boolean uniqueOnly) throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      // Need to find all From Tables with type tableType
      // and extract the Name attributes 
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         TableType[] tables = getAllBaseTables(from);
         int numTables = tables.length;
         Vector tableNames = new Vector();
         for (int i = 0; i < numTables; i++) {
           TableType tableType = tables[i];
            String name = tableType.getName();
            boolean duplicate = false;
            for (int j = 0; j < tableNames.size(); j++) {
               if (name.equals((String)(tableNames.elementAt(j)))) {
                  duplicate = true;
                  break;
               }
            }
            if ((!duplicate) || (!uniqueOnly)) {
               tableNames.addElement(name);
            }    
         }
         numTables = tableNames.size();
         String[] tableStrings = new String[numTables];
         for (int i = 0; i < numTables; i++) {
           tableStrings[i] = (String)tableNames.elementAt(i);
         }
         return tableStrings;
      }
      return new String[0];
   } 

   /** Returns all column names that are used in the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public static String[] getColumnReferences(SelectDocument selectDocument) throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
     // Need to find all SelectionList Items with type columnReferenceType
     // and extract the Name attributes 
      SelectType selectType = selectDocument.getSelect();
      SelectionListType selectionList = selectType.getSelectionList();
      int numItems = selectionList.sizeOfItemArray();
      Vector columnNames = new Vector();
      for (int i = 0; i < numItems; i++) {
         SelectionItemType itemType = selectionList.getItemArray(i);
         if (itemType instanceof ColumnReferenceType) {
            columnNames.addElement(((ColumnReferenceType)itemType).getName());
         }
      }
      numItems = columnNames.size();
      String[] cols = new String[numItems];
      for (int i = 0; i < numItems; i++) {
        cols[i] = (String)columnNames.elementAt(i);
      }
      return cols;
   } 

   /** Returns all column names from the specified table that are used in 
    *  the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public static String[] getColumnReferences(SelectDocument selectDocument, String tableRef) 
         throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      if ((tableRef == null) || ("".equals(tableRef))) {
         throw new QueryException("Input tableRef string  may not be null/empty!");
      }
      // First, get alias (if any) for this specified table
      String tableAlias = getTableAlias(selectDocument, tableRef); // May come back null

      // Now to find all SelectionList Items with type columnReferenceType,
      // and extract the Name attributes for any columns coming from the 
      // specified table.
      SelectType selectType = selectDocument.getSelect();
      SelectionListType selectionList = selectType.getSelectionList();
      int numItems = selectionList.sizeOfItemArray();
      Vector columnNames = new Vector();
      for (int i = 0; i < numItems; i++) {
         SelectionItemType itemType = selectionList.getItemArray(i);
         if (itemType instanceof ColumnReferenceType) {
            if (tableRef.equals(
                  ((ColumnReferenceType)itemType).getTable())) {
               // Only add columns from the specified table
               columnNames.addElement(
                   ((ColumnReferenceType)itemType).getName());
            }
            else if (tableAlias != null) {
              if (tableAlias.equals(
                    ((ColumnReferenceType)itemType).getTable())) {
                 // Only add columns from the specified table
                 columnNames.addElement(
                     ((ColumnReferenceType)itemType).getName());
               }
            }
         }
      }
      numItems = columnNames.size();
      String[] cols = new String[numItems];
      for (int i = 0; i < numItems; i++) {
        cols[i] = (String)columnNames.elementAt(i);
      }
      return cols;
   } 

   /** Returns the alias (if defined) for the specified table name.
    */
   public static String getTableAlias(SelectDocument selectDocument, String tableRef) 
         throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      if ((tableRef == null) || ("".equals(tableRef))) {
         throw new QueryException("Input tableRef string  may not be null/empty!");
      }
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         TableType[] fromTables = XmlBeanUtilities.getAllBaseTables(from);
         int numTables = fromTables.length;
         //int numTables = from.sizeOfTableArray();
         String tableNames[] = new String[numTables];
         for (int i = 0; i < numTables; i++) {
            //TableType tableType = (TableType)(from.getTableArray(i));
            TableType tableType = fromTables[i];
            if (tableType.getName().equals(tableRef)) {
               return tableType.getAlias();
            }
         }
      }
      return null;  // No alias found for specified table.
   }

   /** Returns the catalog attribute (if defined) for the specified table.
    */
   public static String getParentCatalog(TableType tableType) 
      throws QueryException
   {
      if ((tableType == null)) {
         throw new QueryException("Input TableType may not be null!");
      }
      String catalogName = tableType.getCatalog();
      if ((catalogName == null) || ("".equals(catalogName)))  {
         return null;
      }
      return catalogName;
   }

   /** Sets the catalog attribute for the specified table.
    */
   public static void setParentCatalog(TableType tableType, 
         String catalogName, boolean overwrite) 
      throws QueryException
   {
      if ((tableType == null)) {
         throw new QueryException("Input TableType may not be null!");
      }
      if ((catalogName == null) || catalogName.equals("")) {
         throw new QueryException(
               "Input catalogName may not be null or empty!");
      }
      if (overwrite == false) {
         // Check to see if we have an empty catalog name;  if not,
         // no need to do anything
         String currName = tableType.getCatalog();
         if (  (currName != null) && (!currName.equals("")) ) {
            // Already set - don't overwrite
            return;
         }
      }
      // Now overwrite
      tableType.setCatalog(catalogName);
   }

   /** Sets the catalog name for any tables that don't already have a catalog
    * name specified.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public static void setParentCatalogReferences(
         SelectDocument selectDocument, String catalogName, boolean overwrite) 
          throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      // Need to find all From Tables with type tableType
      // and set the catalogNameextract the Name attributes 
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         TableType[] tables = getAllBaseTables(from);
         int numTables = from.sizeOfTableArray();
         Vector parentNames = new Vector();
         for (int i = 0; i < numTables; i++) {
            setParentCatalog(tables[i], catalogName, overwrite);
         }
      }
   } 
   /** Returns a list of all the TableType elements in the specified 
    * FromType (including those buried inside a JoinTableType element). 
    */
   public static TableType[] getAllBaseTables(FromType from) throws QueryException
   {
      Vector fullList = getVectorOfAllBaseTables(from);
      TableType[] emptyList = new TableType[0];
      return (TableType[])fullList.toArray(emptyList);
   }

   protected static Vector getVectorOfAllBaseTables(FromType from) throws QueryException
   {
      Vector fullList = new Vector();
      if (from == null) {
         throw new QueryException("Input FromType may not be null!");
      }
      int numTables = from.sizeOfTableArray();
      for (int i = 0; i < numTables; i++) {
         String name = "";
         String parentCatalog = null;     //Default
         FromTableType fromTable = (FromTableType)(from.getTableArray(i));
         if (fromTable instanceof TableType) {
            fullList.add((TableType)(from.getTableArray(i)));
         }
         else if (fromTable instanceof JoinTableType) {
            JoinTableType tableType = 
               (JoinTableType)(from.getTableArray(i));
            ArrayOfFromTableType tables = tableType.getTables();
            FromTableType[] fromTableArray = tables.getFromTableTypeArray();
            for (int j = 0; j < fromTableArray.length; j++) {
               if (fromTableArray[j] instanceof TableType) {
                  fullList.add((TableType)(fromTableArray[j]));
               }
               else {
                  // KONA TOFIX DO SOMETHING ELSE LATER?
                  throw new QueryException("Got an unrecognised table type from a JoinTableType element - this input query is not supported");
               }
            }
         }
      }
      return fullList;
   }


   /** Returns the real table name for the specified alias (or just 
    * returns the input value if it is actually a table name, not an alias).
    */
   public static String getTableName(SelectDocument selectDocument, String tableAlias) 
         throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      if ((tableAlias == null) || ("".equals(tableAlias))) {
         throw new QueryException("Input tableAlias string  may not be null/empty!");
      }
      SelectType selectType = selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         TableType[] fromTables = XmlBeanUtilities.getAllBaseTables(from);
         int numTables = fromTables.length;
         //int numTables = from.sizeOfTableArray();
         String tableNames[] = new String[numTables];
         for (int i = 0; i < numTables; i++) {
            //TableType tableType = (TableType)(from.getTableArray(i));
            TableType tableType = fromTables[i];
            // Check if we have an alias
            if (tableType.getAlias().equals(tableAlias)) {
               return tableType.getName();
            }
            // Check if the alias is actually a table name
            if (tableType.getName().equals(tableAlias)) {
               return tableAlias;
            }
         }
      }
      return null;  // No name found for specified alias
   }

   /** Uses xmlbeans functionality to validate the query's current 
    * contents against schema. */
   public static void validateAdql(SelectDocument selectDocument) throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      // Validate against schema
      // Set up the validation error listener.
      ArrayList validationErrors = new ArrayList();
      xmlOptions = new XmlOptions();
      xmlOptions.setErrorListener(validationErrors);
      boolean isValid = selectDocument.validate(xmlOptions);
      if (!isValid) {
         String errorString = "Input ADQL is invalid: \n";
         Iterator iter = validationErrors.iterator();
         while (iter.hasNext()) {
            errorString = errorString + iter.next() + "\n";
         }
         throw new QueryException(errorString);
      }
   }
}
