/*
 * $Id: Query.java,v 1.9 2007/03/14 16:26:48 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.Piper;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;

// AG ADQL stuff
import org.astrogrid.adql.AdqlStoX;

// XMLBeans stuff
import org.apache.xmlbeans.* ;
import org.astrogrid.adql.v1_0.beans.*;
// For validation of beans
import java.util.ArrayList;
import java.util.Iterator;

// For legacy DOM interface 
import org.w3c.dom.Element;
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
   private static String NAMESPACE_0_7_4 = 
            "http://www.ivoa.net/xml/ADQL/v0.7.4";
   private static String NAMESPACE_1_0 = 
            "http://www.ivoa.net/xml/ADQL/v1.0";

   // Note: Need to put all the namespace stuff in the ADQL fragments 
   // to have control over the namespace prefices used, etc (particularly
   // important for unit testability).
   // @TOFIX : This isn't maybe the most elegant way to set up the Table
   // entry using xmlbeans. 
   private static String FROM_ADQL =
      "<Table xsi:type=\"tableType\" Alias=\"a\" Name=\"INSERT_TABLE\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\"/>";
   
   /** For xmlbeans validation against schema */
   private static XmlOptions xmlOptions;



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
   public Query(String tableName, String raColName, String decColName,
       double coneRA, double coneDec, double coneRadius, 
       ReturnSpec returnSpec) throws QueryException {
      if (tableName == null) {
         throw new QueryException("Conesearch table name may not be null");
      }
      if (returnSpec == null) {
         throw new QueryException("ReturnSpec may not be null");
      }
      String adqlString = ConeConverter.getAdql(
          tableName, raColName, decColName, 
          coneRA, coneDec, coneRadius);

      StringReader source = new StringReader(adqlString) ;
      try {
         setSelectDocument( 
           (SelectDocument)getCompiler(source).compileToXmlBeans() );
      } 
      catch (Exception e) {
         throw new QueryException("Could not translate conesearch ADQL/sql query into valid ADQL/xml", e);
      }
      this.results = returnSpec;
   }

   /** Constructs a Query using cone-search parameters and 
    * local default table/column names. */
   public Query(double coneRA, double coneDec, double coneRadius, 
          ReturnSpec returnSpec) throws QueryException {
      if (returnSpec == null) {
        throw new QueryException("ReturnSpec may not be null");
      }
      String adqlString = ConeConverter.getAdql(coneRA, coneDec, coneRadius);
      //setSelectDocument(adqlString);
      StringReader source = new StringReader(adqlString) ;
      try {
         setSelectDocument( 
           (SelectDocument)getCompiler(source).compileToXmlBeans() );
      } 
      catch (Exception e) {
         throw new QueryException("Could not translate conesearch ADQL/sql query into valid ADQL/xml", e);
      }
      this.results = returnSpec;
   }

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
      this.results = new ReturnTable(new WriterTarget(new StringWriter()));
   }
   */

   /** Returns the XML query as a string */
   public String getAdqlString()
   {
     return selectDocument.toString();
   }

   /** Returns the XML query as a string suitable for embedding in HTML */
   public String getHtmlAdqlString()
   {
      String xmlString = selectDocument.toString();
      //  Make HTML-display-friendly
      xmlString = xmlString.replaceAll(">", "&gt;");
      xmlString = xmlString.replaceAll("<", "&lt;");
      xmlString = xmlString.replaceAll("\n","<br/>");
      return xmlString;
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
    * Returns maximum number of results specified in the actual query */
   public long getLimit() 
   {      
      if (this.selectDocument != null) {
         SelectType selectType = this.selectDocument.getSelect();
         if (selectType.isSetRestrict()) {
            SelectionLimitType restrict = selectType.getRestrict();
            if (restrict.isSetTop()) {
               return restrict.getTop();
            }
         }
      }
      return LIMIT_NOLIMIT;
   }

   
   /** Returns the lowest of the query limit (stored in the selectDocument) 
    *  or local limit (configured in DSA setup) */
   public long getLocalLimit() {
      long queryLimit = LIMIT_NOLIMIT;  
      long localLimit = ConfigFactory.getCommonConfig().getInt(
            MAX_RETURN_KEY, LIMIT_NOLIMIT);
      SelectType selectType = this.selectDocument.getSelect();
      if (selectType.isSetRestrict()) {
         SelectionLimitType restrict = selectType.getRestrict();
         if (restrict.isSetTop()) {
            queryLimit = restrict.getTop();
         }
      }
      if ((queryLimit == LIMIT_NOLIMIT) || 
             ((queryLimit > localLimit) && (localLimit > 0))) {
         queryLimit = localLimit;
      }
      return queryLimit;
   }
   
   /** Returns all UNIQUE table names that are used in the query.
    *  This can be needed for dealing with VOTable metadata etc.
    */
   public String[] getTableReferences() 
   {
      // Need to find all From Tables with type tableType
      // and extract the Name attributes 
      SelectType selectType = this.selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         int numTables = from.sizeOfTableArray();
         //String tableNames[] = new String[numTables];
         Vector tableNames = new Vector();
         for (int i = 0; i < numTables; i++) {
            // Ooh, naughty casting required!  Scandalous!
            TableType tableType = (TableType)(from.getTableArray(i));
            String name = tableType.getName();
            boolean duplicate = false;
            for (int j = 0; j < tableNames.size(); j++) {
               if (name.equals((String)(tableNames.elementAt(j)))) {
                  duplicate = true;
                  break;
               }
            }
            if (!duplicate) {
               // Only add hitherto unseen references
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
   public String[] getColumnReferences() 
   {
     // Need to find all SelectionList Items with type columnReferenceType
     // and extract the Name attributes 
      SelectType selectType = this.selectDocument.getSelect();
      SelectionListType selectionList = selectType.getSelectionList();
      int numItems = selectionList.sizeOfItemArray();
      Vector columnNames = new Vector();
      for (int i = 0; i < numItems; i++) {
         // Ooh, more casting!  Good thing I'm really a C programmer...
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
   public String[] getColumnReferences(String tableRef) 
   {
      // First, get alias (if any) for this specified table
      String tableAlias = getTableAlias(tableRef); // May come back null

      // Now to find all SelectionList Items with type columnReferenceType,
      // and extract the Name attributes for any columns coming from the 
      // specified table.
      SelectType selectType = this.selectDocument.getSelect();
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
   public String getTableAlias(String tableRef) 
   {
      SelectType selectType = this.selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         int numTables = from.sizeOfTableArray();
         String tableNames[] = new String[numTables];
         for (int i = 0; i < numTables; i++) {
            // Ooh, naughty casting required!  Scandalous!
            TableType tableType = (TableType)(from.getTableArray(i));
            if (tableType.getName().equals(tableRef)) {
               return tableType.getAlias();
            }
         }
      }
      return null;  // No alias found for specified table.
   }

   /** Returns the real table name for the specified alias (or just 
    * returns the input value if it is actually a table name, not an alias).
    */
   public String getTableName(String tableAlias) 
   {
      SelectType selectType = this.selectDocument.getSelect();
      if (selectType.isSetFrom()) {
         FromType from = selectType.getFrom();
         int numTables = from.sizeOfTableArray();
         String tableNames[] = new String[numTables];
         for (int i = 0; i < numTables; i++) {
            // Ooh, naughty casting required!  Scandalous!
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
   public String convertWithXslt(InputStream xsltIn) throws QueryException
   {
      // Temporarily tweak the limit value in the Query to reflect 
      // the lower of the query and datacenter limit (we will restore 
      // it to its original value after the transformation).
      // It would be more elegant just to twiddle with the temporary
      // DOM document generated below, but the beans structure is 
      // just SO much more friendly for these kinds of manipulations...
      long queryLimit = getLimit();
      long localLimit = getLocalLimit();
      boolean changedLimit = false;
      if (queryLimit == LIMIT_NOLIMIT) {  // Don't have a query limit
         if (localLimit != LIMIT_NOLIMIT) { // But do have a datacenter limit
            setLimit(localLimit);
            changedLimit = true;
         }
      }
      else {   // Do have a local limit
         if ((localLimit != LIMIT_NOLIMIT) && (localLimit < queryLimit)) {
            setLimit(localLimit); // Datacenter limit is smaller
            changedLimit = true;
         }
      }

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
        Document beanDom = DomHelper.newDocument(selectDocument.toString());

        // NOTE: Seem to require a DOMSource rather than a StreamSource
        // here or the transformer barfs - no idea why
        // StreamSource source = new StreamSource(adqlBeanDoc.toString());
        DOMSource source = new DOMSource(beanDom);

        // Actually transform the document
        transformer.transform(source, new StreamResult(sw));
        String sql = sw.toString();
        if (changedLimit) { 
           setLimit(queryLimit); // Restore original limit
        }  
        return sql;
      }
      catch (SAXException se) {
         if (changedLimit) { setLimit(queryLimit); }  // Restore original limit
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+se, se);
      }
      catch (TransformerConfigurationException tce) {
         if (changedLimit) { setLimit(queryLimit); }  // Restore original limit
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+tce, tce);
      }
      catch (TransformerException te) {
         if (changedLimit) { setLimit(queryLimit); }  // Restore original limit
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+te, te);
      }
      catch (IOException ioe) {
         if (changedLimit) { setLimit(queryLimit); }  // Restore original limit
         throw new QueryException(
             "Couldn't apply stylesheet to query: "+ioe, ioe);
      }
   }
   
   /**
    * For humans/debugging
    */
   public String toString() {
      StringBuffer s = new StringBuffer("{Query: ");
      if (selectDocument != null) {
        s.append(getAdqlString());
      }
      s.append(" returning "+results+"}");
      return s.toString();
   }
   public String toHTMLString() {
      StringBuffer s = new StringBuffer("{Query: <br/><tt>");
      if (selectDocument != null) {
        s.append(getHtmlAdqlString());
      }
      s.append(" </tt><br/>returning "+results+"}");
      return s.toString();
   }
   
   /** Sets the row limit (including removing it altogether).
    * We should not allow the row limit to be publicly manipulated -
    * this is just for temporarily tweaking the query to reflect the
    * datacenter row limit (if necessary) when converting to SQL.
    */
   private void setLimit(long limit) 
   {      
      SelectType selectType = this.selectDocument.getSelect();
      if (limit == LIMIT_NOLIMIT) { // Remove limit clause altogether
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
   /** Accepts an ADQL/xml query as a string, converts it to xmlbeans
    * representation (including validation against schema) and stores 
    * the beans tree within the Query instance.
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

      //botch fix for linux browsers
      adqlString.replaceAll("</table>", "</Table>");
      adqlString.replaceAll("</select>", "</Select>");

     // Check for expected namespace before trying to parse
      if (adqlString.indexOf(NAMESPACE_1_0) == -1) {
         // Not adql 1.0 
         if (adqlString.indexOf(NAMESPACE_0_7_4) == -1) {
            // Not adql 0.7.4 either, barf
            throw new QueryException(
                "Unrecognised namespace: expecting either " +
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

      // Postprocess to meet our query conventions
      postprocessSelectDocument();
/*
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
         int numTables = from.sizeOfTableArray();
         for (int i = 0; i < numTables; i++) {
            TableType tableType = (TableType)(from.getTableArray(i));
            boolean hasAlias = tableType.isSetAlias();
            if (hasAlias == false) {
              // If no alias, add one same as table name
              // Any column references elsewhere in the query will
              // be referenced by table name, so the pseudo-alias
              // will be consistent with this.
               String name = tableType.getName();
               tableType.setAlias(name);
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
      */
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
         int numTables = from.sizeOfTableArray();
         for (int i = 0; i < numTables; i++) {
            TableType tableType = (TableType)(from.getTableArray(i));
            boolean hasAlias = tableType.isSetAlias();
            if (hasAlias == false) {
              // If no alias, add one same as table name
              // Any column references elsewhere in the query will
              // be referenced by table name, so the pseudo-alias
              // will be consistent with this.
               String name = tableType.getName();
               tableType.setAlias(name);
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
      // Validate against schema
      // Set up the validation error listener.
      ArrayList validationErrors = new ArrayList();
      xmlOptions = new XmlOptions();
      xmlOptions.setErrorListener(validationErrors);
      if (this.selectDocument == null) {
         throw new QueryException("Query ADQL was null, can't validate it!");
      }
      boolean isValid = this.selectDocument.validate(xmlOptions);
      if (!isValid) {
         String errorString = "Input ADQL is invalid: \n";
         Iterator iter = validationErrors.iterator();
         while (iter.hasNext()) {
            errorString = errorString + iter.next() + "\n";
         }
         throw new QueryException(errorString);
      }
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

   /** Creates an ADQL/sql compiler where required, and/or compiles
    * a given ADQL/sql fragment.  */ 
   private AdqlStoX getCompiler(StringReader source) 
   {
     return new AdqlStoX(source);
     /*
      if (compiler == null) {
         compiler = new AdqlStoX(source);
      }
      else {
         compiler.ReInit(source);
      }
      return compiler;
      */
   }
}
