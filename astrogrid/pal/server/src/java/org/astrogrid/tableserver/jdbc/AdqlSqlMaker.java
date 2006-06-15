/*$Id: AdqlSqlMaker.java,v 1.3 2006/06/15 16:50:09 clq2 Exp $
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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

   protected String doXsltTransformation(Query query) throws QueryException
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

      // Now get the query to convert itself with the stylesheet
      return query.convertWithXslt(xsltIn);
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
}
