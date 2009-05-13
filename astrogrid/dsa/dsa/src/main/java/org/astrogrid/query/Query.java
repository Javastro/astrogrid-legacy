/*
 * $Id: Query.java,v 1.1 2009/05/13 13:20:38 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.Piper;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;

// AG ADQL stuff
import org.astrogrid.adql.AdqlCompiler;

// XMLBeans stuff
import org.apache.xmlbeans.* ;
import org.astrogrid.adql.v1_0.beans.*;
/*
// For validation of beans
import java.util.ArrayList;
import java.util.Iterator;
*/

// For legacy DOM interface 
import org.w3c.dom.Element;
import org.astrogrid.xml.DomHelper;

/* For xslt */
/*
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
*/

/**
 * A "native" model of an ADQL query, which includes a representation
 * of the ADQL/xml query itself in xmlbeans, and information about the
 * query output (destination for results, format of results etc).
 *
 * Provides convenience methods for creating cone-search ADQL queries
 * from simple conesearch parameters.
 *
 * Provides additional methods allowing a return row limit to be imposed 
 * on the query.
 *
 * This replaces the original DSA modelled query which didn't cover 
 * ADQL fully enough, and allows us to deprecate the old DSA xml parsers.
 *
 *  @author K Andrews
 *
 *  @TOFIX Need to remove namespace tweak in favour of Jeff's forthcoming
 *  version translation mechanism.
 */

public class Query  {

   /** ADQL query, stored as xmlbeans */
   private SelectDocument selectDocument = null;

   /** Defines what the results will be and where they are to be sent.
    * This is logically separate from the actual query itself, 
    * specifying things like the return format (votable, csv etc)
    * and where the results are to be put (e.g. location in myspace)*/
   private ReturnSpec results = null;

   /** Key used to define maximum number of matches allowed - defaults to 200. 
    * 0 or less = no limit */
   public final static String MAX_RETURN_KEY = "datacenter.max.return";

   /** Constant to indicate that number of rows returned should be unlimited */
   public static final int LIMIT_NOLIMIT = 0; 

   /** Allowed namespaces */
   private final static String NAMESPACE_0_7_4 = 
            "http://www.ivoa.net/xml/ADQL/v0.7.4";
   private final static String NAMESPACE_1_0 = 
            "http://www.ivoa.net/xml/ADQL/v1.0";

   // Note: Need to put all the namespace stuff in the ADQL fragments 
   // to have control over the namespace prefices used, etc (particularly
   // important for unit testability).
   // @TOFIX : This isn't maybe the most elegant way to set up the Table
   // entry using xmlbeans. 
   private final static String FROM_ADQL =
      "<Table xsi:type=\"tableType\" Alias=\"a\" Name=\"INSERT_TABLE\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\"/>";
   
   /** For xmlbeans validation against schema */
   private static XmlOptions xmlOptions;
   
   /**
    * The collection of credentials associated with the query.
    */
   private SecurityGuard guard;

   /* NB THESE ARE FOR FUTURE USE - NOT USED AT THE MOMENT */
   public final static String ADQL_SOURCE = "adql"; 
   public final static String CONE_SOURCE = "cone"; 
   public final static String MULTICONE_SOURCE = "multicone"; 
   public final static String SOURCES[] = new String[] {
      ADQL_SOURCE,
      CONE_SOURCE,
      MULTICONE_SOURCE
   }; 

   private String querySource = ADQL_SOURCE; //Default query source

   /* Shared compiler instance for compiling ADQL/s to ADQL/xml beans.
    * Access to this compiler must be synchronized.
    * NOTE:  NOT CLEAR THAT IT'S MORE EFFICIENT TO HAVE A SINGLE COMPILER:
    * THE OVERHEAD SEEMS TO BE PARSING, NOT IN CREATING THE COMPILER.
   private static AdqlCompiler compiler = null;
   */

   /** Constructs a Query from a string containing ADQL/xml. 
    */
   public Query(String adqlString, ReturnSpec returnSpec) throws QueryException 
   {
      if (returnSpec == null) {
        throw new QueryException("ReturnSpec may not be null");
      }
      setSelectDocument(adqlString);   
      this.results = returnSpec;
   }

   /** Constructs a Query from a pre-initialised SelectDocument.
    *  
    */
   public Query(SelectDocument select) throws QueryException 
   {
      setSelectDocument(select);
      this.results = new ReturnTable(new WriterTarget(new StringWriter()));
   }

   /** Constructs a Query from a string containing ADQL/xml, with
    * a default StringWriter return type. 
    */
   public Query(String adqlString) throws QueryException 
   {
      setSelectDocument(adqlString);
      this.results = new ReturnTable(new WriterTarget(new StringWriter()));
   }

   /** Constructs a Query from a stream containing ADQL/xml. 
    */
   public Query(InputStream adqlStream, ReturnSpec returnSpec) 
          throws QueryException 
   {
      if (returnSpec == null) {
        throw new QueryException("ReturnSpec may not be null");
      }
      StringWriter sw = new StringWriter();
      try {
         Piper.bufferedPipe(new InputStreamReader(adqlStream), sw);
      }
      catch (Exception e) {
        throw new QueryException("Couldn't get ADQL query: "+e, e);
      }
      String adqlString = sw.toString();
      setSelectDocument(adqlString);
      this.results = returnSpec;
   }

   /** Constructs a Query from a stream containing ADQL/xml. 
    */
   public Query(InputStream adqlStream) throws QueryException 
   {
      StringWriter sw = new StringWriter();
      try {
         Piper.bufferedPipe(new InputStreamReader(adqlStream), sw);
      }
      catch (Exception e) {
        throw new QueryException("Couldn't get ADQL query: "+e, e);
      }
      String adqlString = sw.toString();
      setSelectDocument(adqlString);
      this.results = new ReturnTable(new WriterTarget(new StringWriter()));
   }

   /** Constructs a Query from a DOM Element containing ADQL/xml
    * (to support legacy interfaces - may be removed). 
    */
   public Query(Element adqlElement, ReturnSpec returnSpec) 
          throws QueryException 
   {
      if (returnSpec == null) {
        throw new QueryException("ReturnSpec may not be null");
      }
      setSelectDocument(adqlElement);
      this.results = returnSpec;
   }
   /** Constructs a Query from a DOM Element containing ADQL/xml
    * (to support legacy interfaces - may be removed). 
    */
   public Query(Element adqlElement) throws QueryException 
   {
      setSelectDocument(adqlElement);
      this.results = new ReturnTable(new WriterTarget(new StringWriter()));
   }

   /** Constructs a Query using cone-search parameters and 
    * explicit table and column names. */
   public Query(String catalogName, String tableName, String colUnits, String raColName, String decColName,
       double coneRA, double coneDec, double coneRadius, 
       ReturnSpec returnSpec) throws QueryException {
      if (tableName == null) {
         throw new QueryException("Conesearch table name may not be null");
      }
      if (returnSpec == null) {
         throw new QueryException("ReturnSpec may not be null");
      }
      String adqlString = ConeConverter.getAdql(
          catalogName, tableName, colUnits, raColName, decColName, 
          coneRA, coneDec, coneRadius);

      StringReader source = new StringReader(adqlString) ;
      setSelectDocument(compileSelectDocument(source));
      querySource = CONE_SOURCE;
      this.results = returnSpec;
   }

   /** Constructs a Query using cone-search parameters and 
    * local default table/column names. */
   /*
   public Query(double coneRA, double coneDec, double coneRadius, 
          ReturnSpec returnSpec) throws QueryException {
      if (returnSpec == null) {
        throw new QueryException("ReturnSpec may not be null");
      }
      String adqlString = ConeConverter.getAdql(coneRA, coneDec, coneRadius);
      //setSelectDocument(adqlString);
      StringReader source = new StringReader(adqlString) ;
      setSelectDocument(compileSelectDocument(source));
      querySource = CONE_SOURCE;
      this.results = returnSpec;
   }
   */

   /** Constructs a Query using cone-search parameters and 
    * local default table/column names, and default return spec. */
   /*
   public Query(double coneRA, double coneDec, double coneRadius) throws QueryException
   {
      String tableName = 
        ConfigFactory.getCommonConfig().getString("conesearch.table", null);
      if (tableName == null) {
         throw new QueryException("DSA's conesearch.table property was null, but expected valid table name");
      }
      ConeConverter converter = 
          new ConeConverter(coneRA, coneDec, coneRadius, tableName);
      String adqlString = converter.getADQL();
      setSelectDocument(adqlString);
      querySource = CONE_SOURCE;
      this.results = new ReturnTable(new WriterTarget(new StringWriter()));
   }
   */
   /** Sets the source of the query (incoming ADQL by default, but might
    * also be e.g. conesearch or multiconesearch).
    */
   public void setQuerySource(String querySource) throws QueryException {
      for (int i = 0; i < SOURCES.length; i++) {
         if (SOURCES[i].equalsIgnoreCase(querySource)) {
            this.querySource = SOURCES[i];
            return;
         }
      }
      throw new QueryException("Query source identifier '" + querySource
         + "' is not recognized");
   }
   /** Returns an identifier describing the original source of the 
    * query (ADQL, cone etc).
    */
   public String getQuerySource() {
      return querySource;
   }

   /** Returns the XML query as a string */
   public String getAdqlString() throws QueryException
   {
     return XmlBeanUtilities.getAdqlString(this.selectDocument);
   }

   /** Returns the XML query as a string suitable for embedding in HTML */
   public String getHtmlAdqlString() throws QueryException
   {
      return XmlBeanUtilities.getHtmlAdqlString(this.selectDocument);
   }
   
   /**
    * Returns the current TargetIdentifier specifying the query results 
    * destination 
    */
   public TargetIdentifier getTarget()  
   { 
      if (results != null) {
         return results.getTarget(); 
      }
      return null;
   }

   /**
    * Returns the current ReturnSpec (which specifies the query results 
    * destination and return format)
    */
   public ReturnSpec getResultsDef() { return results; }

   /**
    * Sets the ReturnSpec (which specifies the query results 
    * destination and return format)
    */
   public void setResultsDef(ReturnSpec spec) {
      this.results = spec;
   }
   
   /**
    * Reveals the credentials associated with the query.
    * The result of this method is never null but might be a SecurityGuard
    * that contains no useful credentials.
    *
    * @return The credentials (never null).
    */
   public SecurityGuard getGuard() {
     return (this.guard == null)? new SecurityGuard() : this.guard;
   }
   
   /**
    * Records the credentials associated with the query.
    * Passing null credentials erases the memory of credentials previously
    * stored.
    *
    * @param guard The credentials.
    */
   public void setGuard(SecurityGuard guard) {
     this.guard = guard;
   }

   /**
    * Returns maximum number of results specified in the actual query */
   public long getLimit() throws QueryException
   {      
      return XmlBeanUtilities.getLimit(this.selectDocument);
   }

   
   /** Returns the lowest of the query limit (stored in the selectDocument) 
    *  or local limit (configured in DSA setup) */
   public long getLocalLimit() throws QueryException {
      return XmlBeanUtilities.getLocalLimit(this.selectDocument);
   }
   
   /** Returns catalog names for all UNIQUE table references that are used 
    * in the query.
    */
   public String[] getParentCatalogReferences() throws QueryException 
   {
      return XmlBeanUtilities.getParentCatalogReferences(
            this.selectDocument, true);
   } 
   /** Returns catalog names for ALL table references that are used 
    * in the query.
    */
   public String[] getAllParentCatalogReferences() throws QueryException 
   {
      return XmlBeanUtilities.getParentCatalogReferences(
            this.selectDocument, false);
   } 
   /** Returns all UNIQUE table names that are used in the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public String[] getTableReferences() throws QueryException 
   {
      return XmlBeanUtilities.getTableReferences(this.selectDocument, true);
   } 

   /** Returns ALL table names that are used in the query. */
   public String[] getAllTableReferences() throws QueryException 
   {
      return XmlBeanUtilities.getTableReferences(this.selectDocument, false);
   } 

   /** Returns all column names that are used in the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public String[] getColumnReferences() throws QueryException 
   {
      return XmlBeanUtilities.getColumnReferences(this.selectDocument);
   } 

   /** Returns all column names from the specified table that are used in 
    *  the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public String[] getColumnReferences(String tableRef) throws QueryException 
   {
      return XmlBeanUtilities.getColumnReferences(this.selectDocument, tableRef);
   } 

   /** Returns the alias (if defined) for the specified table name.
    */
   public String getTableAlias(String tableRef) throws QueryException 
   {
      return XmlBeanUtilities.getTableAlias(this.selectDocument, tableRef);
   }

   /** Returns the real table name for the specified alias (or just 
    * returns the input value if it is actually a table name, not an alias).
    */
   public String getTableName(String tableAlias) throws QueryException 
   {
      return XmlBeanUtilities.getTableName(this.selectDocument, tableAlias);
   }

   public SelectDocument getSelectDocument() throws QueryException
   {
      if (this.selectDocument == null) {
         throw new QueryException("Query's SelectDocument is currently null!");
      }
      return this.selectDocument;
   }

   /** Sets the catalog prefix (e.g. "catalog.table") to the specified catalog 
    * for ALL table references that don't have one in the query.
    */
   public void setParentCatalogReferences(String catalogName) throws QueryException 
   {
      // false flag = don't overwrite pre-existing catalog names
      XmlBeanUtilities.setParentCatalogReferences(
            this.selectDocument, catalogName, false); 
   } 

   /** Allows an XSLT stylesheet to be applied against the adql query,
    * for example to transform it to sql. 
    * The sql produced reflects the lower of the query row limit and
    * the local datacenter row limit.
    *
    */
   /*
   public String convertWithXslt(InputStream xsltIn) throws QueryException
   {
      return XmlBeanUtilities.convertWithXslt(this.selectDocument, xsltIn);
   }
   */
   
   /**
    * For humans/debugging
    */
   public String toString() {
      StringBuffer s = new StringBuffer("{Query: ");
      if (selectDocument != null) {
        try {
          s.append(getAdqlString());
        }
        catch (QueryException e) {
           // Shouldn't get here anyway
          s.append("[AN ERROR OCCURRED]");
        }
      }
      s.append(" returning "+results+"}");
      return s.toString();
   }
   public String toHTMLString() {
      StringBuffer s = new StringBuffer("{Query: <br/><tt>");
      if (selectDocument != null) {
        try {
          s.append(getHtmlAdqlString());
        }
        catch (QueryException e) {
           // Shouldn't get here anyway
          s.append("[AN ERROR OCCURRED]");
        }
      }
      s.append(" </tt><br/>returning "+results+"}");
      return s.toString();
   }
   
   /** Sets the row limit (including removing it altogether).
    * We should not allow the row limit to be publicly manipulated -
    * this is just for temporarily tweaking the query to reflect the
    * datacenter row limit (if necessary) when converting to SQL.
    */
   private void setLimit(long limit) throws QueryException 
   {      
      XmlBeanUtilities.setLimit(this.selectDocument, limit);
   }

   /** Accepts an ADQL/xml query as a string, converts it to xmlbeans
    * representation (including validation against schema) and stores 
    * the beans tree within the Query instance.  If the string doesn't
    * seem to be valid XML, tries treating it as an ADQL/sql query
    * instead.
    * Includes a botch fix for linux browsers, which have been
    * known to change closing </Table> and </Select> elements 
    * to lower case.
    */
   private void setSelectDocument(String adqlStringIn) throws QueryException
   {
      if (adqlStringIn == null) { 
         throw new QueryException("Input adql string may not be null");
      }
      String adqlString = adqlStringIn.trim();
      if (adqlString.equals("")) {
         throw new QueryException("Input adql string may not be null/empty");
      }

      // Check if we have adql/sql input first 
      if (adqlString.toLowerCase().indexOf("select>") == -1) {
         //Do we have an ADQL/s string instead?
         StringReader source = new StringReader(adqlString) ;
         this.selectDocument = compileSelectDocument(source);
      }
      else {
         // Try XML
         
         //botch fix for linux browsers
         adqlString.replaceAll("</table>", "</Table>");
         adqlString.replaceAll("</select>", "</Select>");

        // Check for expected namespace before trying to parse
         if (adqlString.indexOf(NAMESPACE_1_0) == -1) {
            // Not adql 1.0 
            if (adqlString.indexOf(NAMESPACE_0_7_4) == -1) {
            // Not adql 0.7.4 either 
               // Not adql 0.7.4 either, barf
               throw new QueryException(
                   "Unrecognised ADQL/xml namespace: expecting either " +
                   NAMESPACE_0_7_4 + " or " +
                   NAMESPACE_1_0);
            }
            else {
               // Move 0.7.4 into 1.0 (TOFIX temporary until we have translator!)
               adqlString = tweakNamespace(adqlString);
            }
         }
         // Now, parse the XML into an xmlbeans structure.  
         // This step DOESN'T validate against schema.
         try {
            this.selectDocument = SelectDocument.Factory.parse(adqlString.trim());
         }
         catch (XmlException e) {
           throw new QueryException("Couldn't parse ADQL: " + e, e);
         }

      }
      // Postprocess to meet our query conventions
      postprocessSelectDocument();
   }

   /** Accepts an ADQL/xml query as a DOM Element, converts it to xmlbeans
    * representation (including validation against schema) and stores 
    * the beans tree within the Query instance (to support legacy 
    * interfaces - may be removed). 
    */
   private void setSelectDocument(Element adqlElement) throws QueryException
   {
      String adqlString;
      // Extract the XML
      try {
         adqlString = 
            tweakNamespace(DomHelper.ElementToString(adqlElement)); 
      }
      catch (IOException e) {
        throw new QueryException("Failed to process ADQL Document: " + e, e);
      }
      setSelectDocument(adqlString);
   }

   /** Accepts an ADQL/xml query as precompiled Adqlbeans SelectDocument.
    */
   private void setSelectDocument(SelectDocument document) throws QueryException
   {
      this.selectDocument = document;
      // Postprocess to meet our query conventions
      postprocessSelectDocument();
   }

   /** 
    */
   private void postprocessSelectDocument() throws QueryException
   {
      try {
        validateAdql();
      }
      catch (QueryException e) {
        this.selectDocument = null; // Kill the adql
        throw e;
      }
      // Make sure a FROM clause is present - add a default one
      // if none is present and "default.table" property is set,
      // otherwise reject query.
      SelectType selectType = this.selectDocument.getSelect();
      if (!selectType.isSetFrom()) {
         String defaultTable = 
            ConfigFactory.getCommonConfig().getString("default.table", null);
         if (defaultTable == null) {
           throw new QueryException(
             "No default table specified in DSA configuration - please specify in your ADQL query which table you wish to use");
         }
         String fromString = FROM_ADQL.replaceAll("INSERT_TABLE",defaultTable);
         try {
            selectType.setFrom( FromType.Factory.parse(fromString));
         }
         catch (org.apache.xmlbeans.XmlException xE) {
           throw new QueryException("Couldn't parse ADQL: " + xE, xE);
         }
         //Check still valid
         try {
            validateAdql();
         }
         catch (QueryException e) {
            this.selectDocument = null; // Kill the adql
            throw e;
         }
      }
      else {
         // TOFIX BETTER (Later) Check already-present From clauses, and 
         // add aliases to any that don't have aliases.  This is a quick
         // fix, find a more flexible solution later?  We have a problem
         // with resolving aliases/table names when no alias is supplied
         // in the From clause, leading to a null pointer error.
         FromType from = selectType.getFrom();
         TableType[] fromTables = XmlBeanUtilities.getAllBaseTables(from);
         int numTables = fromTables.length;
         for (int i = 0; i < numTables; i++) {
            TableType tableType = fromTables[i];
            boolean hasAlias = tableType.isSetAlias();
            if (hasAlias == false) {
              // If no alias, add one same as table name
              // Any column references elsewhere in the query will
              // be referenced by table name, so the pseudo-alias
              // will be consistent with this.
              // This includes a quick hack to remove any catalog
              // prefix in a qualified table name, although we
              // wouldn't expect to see one there (any catalog prefix
              // is now stored as an extra optional attribute)
               String name = tableType.getName();
               int dotIndex = name.lastIndexOf('.');
               if (dotIndex != -1) {   //Dot found (
                  tableType.setAlias(name.substring(dotIndex+1));
               }
               else {
                  tableType.setAlias(name);
               }
            }
            // Check for blank aliases and reject the query if the alias
            // is set to whitespace or an empty string - because it's not
            // clear how to interpret any subsequent column references
            // if there is a blank alias
            else {
              String alias = tableType.getAlias();
              if ((alias == null) || (alias.trim().equals("")) ) {
                // reject 
                throw new QueryException(
                    "Empty alias supplied for table " +
                    tableType.getName() + 
                    " - alias must not be empty.");
              }
            }
         }
      }
   }

   /** Uses xmlbeans functionality to validate the query's current 
    * contents against schema. */
   private void validateAdql() throws QueryException
   {
      XmlBeanUtilities.validateAdql(this.selectDocument);
   }

   /** Converts ADQL to expected version by tweaking the namespace 
    * (temporary hack).
    * Currently accepts ADQL 0.7.4 and 1.0;  moves 0.7.4 
    * queries into the 1.0 namespace (to be replaced).
    * @TOFIX : Replace namespace change with Jeff's translation
    * mechanism.
    */
   private String tweakNamespace(String adqlString) throws QueryException
   {
     // Check for expected namespace before trying to parse
      if (adqlString.indexOf(NAMESPACE_1_0) == -1) {
         // Not adql 1.0 
         if (adqlString.indexOf(NAMESPACE_0_7_4) == -1) {
            // Not adql 0.7.4 either, barf
            throw new QueryException(
                "Unrecognised namespace: expecting either " +
                NAMESPACE_0_7_4 + " or " +
                NAMESPACE_1_0
            );
         }
         else {
            // Got 0.7.4, change namespace to 1.0
            adqlString = adqlString.replaceAll(
               "http://www\\.ivoa\\.net/xml/ADQL/v0\\.7\\.4",
               "http://www.ivoa.net/xml/ADQL/v1.0"
            );
         }
      }
      return adqlString;
   }

   /** Compiles a given ADQL/sql fragment into ADQL/xml beans.
    */
   private SelectDocument compileSelectDocument(StringReader source) 
      throws QueryException
   {
      synchronized (Query.class) {
         try {
            /*
            if (compiler == null) {
               compiler = new AdqlCompiler(source);
            }
            else {
               compiler.ReInit(source);
            }
            */
            AdqlCompiler compiler = new AdqlCompiler(source);
            return (SelectDocument) compiler.compileToXmlBeans();
         }
         catch (Exception e) {
            throw new QueryException("Could not translate conesearch ADQL/sql query into valid ADQL/xml", e);
         }
      }
   }
}
