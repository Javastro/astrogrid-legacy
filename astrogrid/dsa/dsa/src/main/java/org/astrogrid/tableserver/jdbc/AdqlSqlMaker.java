/*$Id: AdqlSqlMaker.java,v 1.2 2011/05/05 14:49:38 gtr Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.tableserver.jdbc;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.astrogrid.cfg.ConfigFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.astrogrid.xml.DomHelper;

// Query stuff
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.XmlBeanUtilities;

// XMLBeans stuff
import org.astrogrid.adql.beans.ColumnReferenceType;
import org.astrogrid.adql.beans.FromType;
import org.astrogrid.adql.beans.SelectDocument;
import org.astrogrid.adql.beans.SelectType;
import org.astrogrid.adql.beans.SelectionListType;
import org.astrogrid.adql.beans.TableType;

import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.NameTranslator;
import org.astrogrid.tableserver.metadata.MetadocNameTranslator;


/**
 * A translator that extracts SQL from a query. 
 * This uses functionality supplied by the Query instance to translate itself
 * to SQL, using a stylesheet specified by a DSA configuration property.
 * The AdqlSqlMaker should therefore be a fairly generic RDBMS plugin,
 * flavoured for particular systems by choice of stylesheet.
 *
 * For weird RDBMS SQL flavours (where extra tweaking is required that 
 * can't be done in XSLT), this class can be subclassed.
 *
 * @author M Hill
 * @author K Andrews
 */
public class AdqlSqlMaker implements SqlMaker {

   private static final Log log = LogFactory.getLog(AdqlSqlMaker.class);
   private static TransformerFactory tFactory = null; 
   private static Transformer transformer = null; 
   
   /**
    * Constructs an SQL statement for the given Query.  Uses the ADQL generator
    * and XSLT style sheets - there may be a better way of doing this!
    */
   public String makeSql(Query query) throws QueryException {
     // Descendant classes might do something extra in here
      String sql = doXsltTransformation(query);
     // Or in here
     return sql;
   }


   /**
    * Constructs an SQL count statement for the given Query.
    */

   /**
    * Constructs an SQL count statement for the given Query.
    * @TOFIX KEA SAYS: This is a truly disgusting hack which won't
    * work with ADQL queries with complex expression selections as 
    * aliased columns.  It may also fail if "select" or "from" appears
    * in unexpected places (e.g. as part of column / table names).
    * However, this method is intended for administrative/testing 
    * use only (I'm going to remove it from the public Datacenter
    * API) so I will leave it here for now.
    * @deprecated Because it's horrible/fragile
    */
   public String makeCountSql(Query query) throws QueryException {

      //get ordinary SQL
      String sql = makeSql(query);
      String lowercaseSql = sql.toLowerCase();
      
      //remove anything between SELECT and FROM and replace with COUNT(*)
      // Use lowercased sql to find indices
      int selectIdx = lowercaseSql.indexOf("select");
      int fromIdx = lowercaseSql.indexOf("from");
      String countSql = sql.substring(0,selectIdx+6)+" COUNT(*) "+sql.substring(fromIdx);
      return countSql;
   }
   /*
    * This version will count the rows in a table; it's likely
    * to be more reliable, but doesn't reflect the original query constraints.
   public String makeCountSql(Query query) throws QueryException {

      String tableNames[] = query.getTableReferences();
      if (tableNames.length == 0) {
         // This should never happen, as Query class creates a default
         // FROM table if one is not given in the input ADQL.
         throw new QueryException(
           "Unexpected error - query contained no table references!");
      }
      if (tableNames.length > 1) {
         throw new QueryException(
          "Cannot produce count(*) SQL from query referencing multiple tables");
      }
      // Once we get here, we have 1 table name only
      String countSql = "SELECT COUNT(*) FROM " + tableNames[0] + ";";
      return countSql;
   }
   */


   /** Allows an XSLT stylesheet to be applied against the adql query,
    * for example to transform it to sql. 
    * The sql produced reflects the lower of the query row limit and
    * the local datacenter row limit.
    *
    */
   protected String doXsltTransformation(Query query) throws QueryException
   {
      if (query == null) {
         throw new QueryException("Input Query may not be null!");
      }
      SelectDocument selectDocument = query.getSelectDocument();
      if (selectDocument == null) {
         // Shouldn't get here
         throw new QueryException("Input Query may not be empty!");
      }
      // Lazy initialization of transformer factory
      synchronized (AdqlSqlMaker.class) {
         if (tFactory == null) {
   	      tFactory = TransformerFactory.newInstance();
            try {
               tFactory.setAttribute("UseNamespaces", Boolean.FALSE);
            }
            catch (IllegalArgumentException iae) {
               // From MCH:  Ignore - if UseNamespaces is unsupported, 
               // it will chuck an exception, and we don't want 
               // to use namespaces anyway so that's fine
            }
         }
         if (transformer == null) {
            InputStream xsltIn = getXsltIn();
            if (xsltIn == null) {
               // Shouldn't get here
               throw new QueryException(
                     "Input XSLT InputStream may not be null!");
            }
            try {
               transformer = 
                  tFactory.newTransformer(new StreamSource(xsltIn));
            }
            catch (TransformerConfigurationException tce) {
               throw new QueryException(
                  "Couldn't apply stylesheet to query: "+tce, tce);
            }
         }
      }
      SelectDocument queryClone = (SelectDocument)selectDocument.copy(); 
      // Tweak the limit value in the cloned query to reflect 
      // the lower of the query and datacenter limit.
      
      long queryLimit = XmlBeanUtilities.getLimit(selectDocument);
      long localLimit = XmlBeanUtilities.getLocalLimit(selectDocument);
      if (queryLimit == Query.LIMIT_NOLIMIT) {  // Don't have a query limit
         if (localLimit != Query.LIMIT_NOLIMIT) { // But do have a datacenter limit
            XmlBeanUtilities.setLimit(queryClone, localLimit);
         }
      }
      else {   // Do have a local limit
         if ((localLimit != Query.LIMIT_NOLIMIT) && (localLimit < queryLimit)) {
            XmlBeanUtilities.setLimit(queryClone, localLimit); // Datacenter limit is smaller
         }
      }

      // Translate from public names in the professed DB-schema to internal
      // names in the actual schema.
      queryClone = applyNameTransformations(queryClone);

      try {
        //Transformer transformer = 
        StringWriter sw = new StringWriter();

        // Extract the query as a Dom document
        Document beanDom = DomHelper.newDocument(queryClone.toString());

        // NOTE: Seem to require a DOMSource rather than a StreamSource
        // here or the transformer barfs - no idea why
        // StreamSource source = new StreamSource(adqlBeanDoc.toString());
        DOMSource source = new DOMSource(beanDom);

        // Actually transform the document
        synchronized (AdqlSqlMaker.class) {
           // Individual transformers can be re-used, but are not
           // threadsafe
           transformer.transform(source, new StreamResult(sw));
        }
        String sql = sw.toString();
        log.debug("Result of ADQL->SQL transformation is:\n" + sql);
        return sql;
      }
      catch (SAXException se) {
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+se, se);
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

   protected InputStream getXsltIn() throws QueryException
   {
      InputStream xsltIn = null;
      String key = "datacenter.sqlmaker.xslt";
      String whereIsDoc = "";

      String xsltDoc = ConfigFactory.getCommonConfig().getString(key, null);
      if ((xsltDoc == null) || (xsltDoc.trim().equals(""))) {
          throw new QueryException(
              "Property 'datacenter.sqlmaker.xslt' is not configured,"+
              " cannot translate to sql.");
      }

      // First try to find file directly (in case it's a filesystem file)
      log.debug("Trying to load XSLT as filesystem file: " + xsltDoc); 
      File file = new File(xsltDoc);
      if (file.exists()) {
        try {
          xsltIn = new FileInputStream(xsltDoc);
        }
        catch (FileNotFoundException e) {
        }
      }

      if (xsltIn == null) {
         //find specified sheet as resource of this class
         xsltIn = AdqlSqlMaker.class.getResourceAsStream("./xslt/"+xsltDoc);
         whereIsDoc = AdqlSqlMaker.class+" resource ./xslt/"+xsltDoc;
         log.debug("Trying to load XSLT as resource of class: " +whereIsDoc);
      }

      //if above doesn't work, try doing by hand for Tomcat ClassLoader
      if (xsltIn == null) {
         // Extract the package name, turn it into a filesystem path by 
         // replacing .'s with /'s, and append the path to the xslt.
         // Hopefully this will mean tomcat can locate the file within
         // the bundled jar file.  
         // NB: Comment below about tomcat not finding it via this method
         // *may* be incorrect - previously this bit of code used package's
         // toString() method, not getName() method, which added
         // extra stuff like package description into the returned string
         // and thereby messed up the file path conversion. 
         String path = 
           AdqlSqlMaker.class.getPackage().getName().replace('.', '/') +
               "/xslt/" + xsltDoc;
         xsltIn = AdqlSqlMaker.class.getClassLoader().getResourceAsStream(path);
         log.debug("Trying to load XSLT via classloader : " +path);
      }

      //Sometimes it won't even find it then if it's in a JAR.  
      //Look in class path.  However *assume* it's in classpath, 
      //as we don't know what the classpath is during unit tests.
      if (xsltIn == null) {
         log.warn("Could not find builtin ADQL->SQL transformer doc '"
             +whereIsDoc+"', looking in classpath...");

         xsltIn = this.getClass().getClassLoader().getResourceAsStream(
                "xslt/"+xsltDoc);
         whereIsDoc = "xslt/" + xsltDoc+" in classpath at "+
           this.getClass().getClassLoader().getResource(xsltDoc);
         log.debug("Trying to load XSLT via classpath : " +whereIsDoc);
      }

      if (xsltIn == null) {
          throw new QueryException(
              "Could not find ADQL->SQL transformer doc "+xsltDoc);
      }

      //use config-specified sheet
      /*
      FileInputStream xsltIn;
      try {
         xsltIn = new FileInputStream(xsltDoc);
      }
      catch (java.io.FileNotFoundException e) {
          throw new QueryException(
              "Unable to open stylesheet file '" + xsltDoc +
              "', cannot translate to sql.");
      }
      */
      return xsltIn;
   }

   /** foo */
   protected SelectDocument applyNameTransformations(SelectDocument selectDocument) 
      throws QueryException
   {
      if (selectDocument == null) {
         throw new QueryException("Input SelectDocument may not be null!");
      }

      log.debug("TRANSFORMING NAMES...\nINITIAL QUERY IS: \n" 
            + selectDocument.toString());

      // KONA TOFIX LATER: SHOULD CHECK CONFIG PARAM FOR WHAT TO USE
      NameTranslator translator = new MetadocNameTranslator();

      // Make sure a FROM clause is present - add a default one
      // if none is present and "default.table" property is set,
      // otherwise reject query.
      SelectType selectType = selectDocument.getSelect();
      if (!selectType.isSetFrom()) {
         throw new QueryException("Input SelectDocument must have a FROM clause!");
      }
      FromType from = selectType.getFrom();
      TableType[] fromTables = XmlBeanUtilities.getAllBaseTables(from);
      int numTables = fromTables.length;
      try {
         for (int i = 0; i < numTables; i++) {
            TableType tableType = fromTables[i];
            String tableName = tableType.getName(); 
            // Strip any catalog prefix off table name, just in case
            int dotIndex = tableName.lastIndexOf(".");
            if (dotIndex > 0) {
               tableName = tableName.substring(dotIndex+1);
            }
            // Check for catalog name prefix - may be null
            String catalogName = XmlBeanUtilities.getParentCatalog(tableType);
            String tableAlias = XmlBeanUtilities.getTableAlias(
                  selectDocument,tableName); // May come back null
            tableType.setName(
                  translator.getTableRealname(catalogName,tableName));
            if (tableType.isSetCatalog()) {
               // First check if we need to remove the archive to hide
               // it from the sql backend.  If we only have one database
               // exposed (e.g. in SampleStars config), we don't need 
               // the catalog name.
               String hideCat = "true";   //By default
               int numCats = translator.getNumCatalogs();
               if (numCats > 1) {
                  try {
                     hideCat = ConfigFactory.getCommonConfig().getString(
                          "datacenter.plugin.jdbc.hidecatalog","false");
                  }
                  catch (Exception e) {
                    // Ignore if not found
                  }
               }
               if ("true".equals(hideCat) || "TRUE".equals(hideCat)) {
                  //Don't want to expose the catalog (schema) name
                  tableType.unsetCatalog();
               }
               else {
                  String archiveName = tableType.getCatalog();
                  tableType.setCatalog(translator.getCatalogRealname(catalogName));
                 
               }
            }
            // Now to find all SelectionList Items of type columnReferenceType,
            // and extract the Name attributes for any columns coming from the 
            // specified table.
            SelectionListType selectionList = selectType.getSelectionList();
            try {
              selectionList.save(System.out);
            }
            catch (IOException e) {
              // Ignore
            }
            if (selectionList == null) { // CAN THIS BE NULL??
               throw new QueryException(
                     "Input SelectionList must not be NULL!");
            }
            ColumnReferenceType[] columns =
                     getColumnReferences(selectDocument);
            for (int j = 0; j < columns.length; j++) {
              if (isInThisTable(tableName, tableAlias, columns[j])) {
                String newName = translator.getColumnRealname(catalogName,
                                                              tableName,
                                                              columns[j].getName());
                columns[j].setName(newName);
              }
            }
         }
      }
      catch (MetadataException me) {
         throw new QueryException(
               "Couldn't perform query name transformations: " + 
               me.getMessage(), me);
      }
      log.debug("FINAL QUERY IS: \n" + selectDocument.toString());
      return selectDocument;
   }

   protected ColumnReferenceType[] getColumnReferences(SelectDocument doc) 
         throws QueryException {
      ArrayList colRefs = new ArrayList() ;
      //
      // Loop through the whole of the query looking for 
      // ColumnReferenceType(s)....
      XmlCursor cursor = doc.newCursor() ;
      try {
        while( !cursor.toNextToken().isNone() ) {
           if( cursor.isStart()  && 
              (cursor.getObject().schemaType() == ColumnReferenceType.type )){
              colRefs.add( cursor.getObject() ) ;
           }
        } // end while
      }
      catch( Exception ex ) {
         log.error("Warning:  Failed to get all column references from SelectDocument;  error was " + ex.toString());
         throw new QueryException("Failed to get column references from SelectDocument", ex);
      }
      finally {
          cursor.dispose() ;
      }
      return (ColumnReferenceType[])colRefs.toArray( new ColumnReferenceType[ colRefs.size() ] ) ;
  }


  /**
   * Determines whether a given column is associated with a named table.
   * The metadata are fickle here so we include four cases giving a positive result:
   * <ul>
   * <li>column's professed table-name matches the name of the given table;</li>
   * <li>column's professed table-name matches the alias of the given table;</li>
   * <li>column's professed table-name matches the name of the given table;</li>
   * <li>column's doesn't know what table it's in;</li>
   * <li>we have neither name nor alias for the given table.</li>
   * </ul>
   * The last two cases are thought to arise only if there is a single table, so
   * "true" is the best guess. However, these kludges invite bugs.
   *
   * @param tableName The name of the given table.
   * @param tableAlias The alias of the given table.
   * @param column The metadata for the column.
   * @return True if the column is associated with the given table.
   */
  protected boolean isInThisTable(String tableName,
                                  String tableAlias,
                                  ColumnReferenceType column) {
    String tableForColumn = column.getTable();
    return (tableForColumn == null ||
            (tableName == null && tableAlias == null) ||
            tableForColumn.equals(tableName) ||
            tableForColumn.equals(tableAlias));
  }
  
}
