/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import java.io.StringWriter;
import java.io.InputStream;
import java.util.Vector;
import org.astrogrid.cfg.ConfigFactory;

// XMLBeans stuff
import org.apache.xmlbeans.* ;
import org.astrogrid.adql.v1_0.beans.*;

// For validation of beans
import java.util.ArrayList;
import java.util.Iterator;

// For legacy DOM interface 
import org.astrogrid.xml.DomHelper;

/* For xslt */
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


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
         int numTables = from.sizeOfTableArray();
         Vector parentNames = new Vector();
         for (int i = 0; i < numTables; i++) {
            String name = "";
            String parentCatalog = null;     //Default
            FromTableType fromTable = (FromTableType)(from.getTableArray(i));
            if (fromTable instanceof TableType) {
               TableType tableType = 
                  (TableType)(from.getTableArray(i));
               name = tableType.getName();
               parentCatalog = getParentCatalog(tableType);
            }
            else if (fromTable instanceof JoinTableType) {
               /*
               JoinTableType tableType = 
                  (JoinTableType)(from.getTableArray(i));
               name = tableType.getName();
               */
               throw new QueryException("This DSA/catalog installation " +
                     "can not handle queries involving joins");
            }
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
         int numTables = from.sizeOfTableArray();
         //String tableNames[] = new String[numTables];
         Vector tableNames = new Vector();
         for (int i = 0; i < numTables; i++) {
            TableType tableType = (TableType)(from.getTableArray(i));
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
         String[] tables = new String[numTables];
         for (int i = 0; i < numTables; i++) {
           tables[i] = (String)tableNames.elementAt(i);
         }
         return tables;
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
         int numTables = from.sizeOfTableArray();
         String tableNames[] = new String[numTables];
         for (int i = 0; i < numTables; i++) {
            TableType tableType = (TableType)(from.getTableArray(i));
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
      String catalogName = tableType.getArchive();
      if ((catalogName == null) || ("".equals(catalogName)))  {
         return null;
      }
      return catalogName;
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
         int numTables = from.sizeOfTableArray();
         String tableNames[] = new String[numTables];
         for (int i = 0; i < numTables; i++) {
            TableType tableType = (TableType)(from.getTableArray(i));
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


   /** Allows an XSLT stylesheet to be applied against the adql query,
    * for example to transform it to sql. 
    * The sql produced reflects the lower of the query row limit and
    * the local datacenter row limit.
    *
    */
   /*
   public static String convertWithXslt(SelectDocument selectDocument, InputStream xsltIn) 
         throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      if (xsltIn == null) {
         throw new QueryException("Input XSLT InputStream may not be null!");
      }

      SelectDocument queryClone = (SelectDocument)selectDocument.copy(); 
      // Tweak the limit value in the cloned query to reflect 
      // the lower of the query and datacenter limit.
      
      long queryLimit = getLimit(selectDocument);
      long localLimit = getLocalLimit(selectDocument);
      if (queryLimit == Query.LIMIT_NOLIMIT) {  // Don't have a query limit
         if (localLimit != Query.LIMIT_NOLIMIT) { // But do have a datacenter limit
            setLimit(queryClone, localLimit);
         }
      }
      else {   // Do have a local limit
         if ((localLimit != Query.LIMIT_NOLIMIT) && (localLimit < queryLimit)) {
            setLimit(queryClone, localLimit); // Datacenter limit is smaller
         }
      }
      applyNameTransformations(queryClone);

   	TransformerFactory tFactory = TransformerFactory.newInstance();
      try {
         tFactory.setAttribute("UseNamespaces", Boolean.FALSE);
      }
      catch (IllegalArgumentException iae) {
         // From MCH:  Ignore - if UseNamespaces is unsupported, 
         // it will chuck an exception, and we don't want 
         // to use namespaces anyway so that's fine
      }
      try {
        Transformer transformer = 
          tFactory.newTransformer(new StreamSource(xsltIn));
        StringWriter sw = new StringWriter();

        // Extract the query as a Dom document
        Document beanDom = DomHelper.newDocument(queryClone.toString());

        // NOTE: Seem to require a DOMSource rather than a StreamSource
        // here or the transformer barfs - no idea why
        // StreamSource source = new StreamSource(adqlBeanDoc.toString());
        DOMSource source = new DOMSource(beanDom);

        // Actually transform the document
        transformer.transform(source, new StreamResult(sw));
        String sql = sw.toString();
        return sql;
      }
      catch (SAXException se) {
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+se, se);
      }
      catch (TransformerConfigurationException tce) {
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+tce, tce);
      }
      catch (TransformerException te) {
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+te, te);
      }
      catch (IOException ioe) {
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+ioe, ioe);
      }
   }
*/
   

   /*
   public static void applyNameTransformations(SelectDocument selectDocument) 
      throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }
      // TOFIX SHOULD CHECK CONFIG PARAM FOR WHAT TO USE
      NameTranslator translator = new MetadocNameTranslator();

      // Make sure a FROM clause is present - add a default one
      // if none is present and "default.table" property is set,
      // otherwise reject query.
      SelectType selectType = selectDocument.getSelect();
      if (!selectType.isSetFrom()) {
         throw new QueryException("Input SelectDocument must have a FROM clause!");
      }
      FromType from = selectType.getFrom();
      int numTables = from.sizeOfTableArray();
      for (int i = 0; i < numTables; i++) {
         TableType tableType = (TableType)(from.getTableArray(i));
         String tableName = tableType.getName(); 
         String tableAlias = getTableAlias(selectDocument,tableName); // May come back null
         // KONA TOFIX null toe be replaced by catalog name
         String tableName = tableType.setName(
            translator.getTableRealname(null,tableName));

         // Now to find all SelectionList Items with type columnReferenceType,
         // and extract the Name attributes for any columns coming from the 
         // specified table.
         SelectionListType selectionList = selectType.getSelectionList();
         if (selectionList == null) { // CAN THIS BE NULL??
            throw new QueryException("Input SelectionList must not be NULL!");
         }
         int numItems = selectionList.sizeOfItemArray();
         Vector columnNames = new Vector();
         for (int j = 0; j < numItems; j++) {
            SelectionItemType itemType = selectionList.getItemArray(j);
            if (itemType instanceof ColumnReferenceType) {
               if (tableName.equals(
                     ((ColumnReferenceType)itemType).getTable())) {
                  String columnName = ((ColumnReferenceType)itemType).getName();
                  System.out.println("WE HAVE COLUMN " + columnName + " FOR TABLE " +
                        tableName);
                  // KONA TOFIX nulls to be replaced by catalog name
                  ((ColumnReferenceType)itemType).setName(
                       translator.getColumnRealname(null,tableName,columnName));
               }
               else if (tableAlias != null) {
                 if (tableAlias.equals(
                       ((ColumnReferenceType)itemType).getTable())) {
                     String columnName = ((ColumnReferenceType)itemType).getName();
                     System.out.println("WE HAVE COLUMN " + columnName + " FOR TABLE " +
                        tableAlias);
                     // KONA TOFIX nulls to be replaced by catalog name
                     ((ColumnReferenceType)itemType).setName(
                       translator.getColumnRealname(null,tableName,columnName));
                  }
               }
            }
         }
      }
   }
   */

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
