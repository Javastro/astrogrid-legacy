<%@ page language="java"
    import="java.util.*, org.astrogrid.cfg.ConfigFactory, java.io.*,
    org.astrogrid.query.returns.*,
    org.astrogrid.query.condition.*,
    org.astrogrid.query.*,
    org.astrogrid.query.adql.*,
    org.apache.commons.logging.LogFactory,
    org.astrogrid.dataservice.metadata.*, org.astrogrid.dataservice.service.ServletHelper,
    org.astrogrid.dataservice.service.DataServer,
    org.w3c.dom.* " %>

<%-- This page takes the entries from the queryBuilder forms, creates an ADQL/xml
     query from it, and forwards the user to the ADQL/xml entry form page to
     submit it --%>
<html>
<head></head>
<body>
Creating query...

<hr>
<h2>Debug</h2>
Parameter names in this request:
<p>
<pre>
<%
   Enumeration e = request.getParameterNames();
   while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      String[] values = request.getParameterValues(key);
      out.print(" " + key + " = ");
      for(int i = 0; i < values.length; i++) {
         out.print(values[i] + " ");
      }
      out.println();
   }
%>
</pre>

<%
   
   String adqlXml = "";
   
   try {
      out.flush();
      String[] searchCols = request.getParameterValues("searchColumn");
      if (searchCols == null) { searchCols = new String[0]; }  //don't have to check for nulls
      String[] resultCols = request.getParameterValues("resultColumn");
      if (resultCols == null) { resultCols = new String[] {"*"}; }
      String[] conditionColumns = request.getParameterValues("conditionColumn");
      if (conditionColumns == null) { conditionColumns = new String[0]; }
      String[] conditionOperands = request.getParameterValues("conditionOperand");
      if (conditionOperands == null) { conditionOperands = new String[0]; }
      String[] conditionValues = request.getParameterValues("conditionValue");
      if (conditionValues == null) { conditionValues = new String[0]; }
   
      Condition criteria = null;
      //create modelled query
      for (int con=0;con<conditionColumns.length;con++) {
       
         String colRef = conditionColumns[con];
   
         if ((colRef != null) && (colRef.trim().length() >0) && (con<conditionOperands.length) && (con<conditionValues.length)) {
            String tableName = "";
            String colName = "";
            if (colRef.indexOf(".")==-1) {
               colName = colRef;
            }
            else {
               //split table/column
               tableName = colRef.substring(0, colRef.indexOf("."));
               colName = colRef.substring(colRef.indexOf(".")+1);
            }
            
            String operand = "";
            if (conditionOperands[con].equals("GT")) operand = ">";
            else if (conditionOperands[con].equals("GTE")) operand = ">=";
            else if (conditionOperands[con].equals("LT"))  operand = "<";
            else if (conditionOperands[con].equals("LTE")) operand = "<=";
            else if (conditionOperands[con].equals("EQ")) operand = "=";
            else if (conditionOperands[con].equals("LIKE")) operand = "LIKE";
            
            Condition newCriteria = null;
            try {
               newCriteria = new NumericComparison(
                           new ColumnReference(tableName,colName),
                           operand,
                           new LiteralReal(conditionValues[con])
                        );
            }
            catch (NumberFormatException nfe) {
               //try string
               newCriteria = new StringComparison(
                           new ColumnReference(tableName,colName),
                           operand,
                           new LiteralString(conditionValues[con])
                        );
               
            }
            
            if (criteria == null) {
               criteria = newCriteria;
            }
            else {
               if (request.getParameter("combine") == null) {
                  out.write("ERROR: 'Combine' not selected - select 'Intersection' or 'Union'");
                  out.flush();
                  throw new IllegalArgumentException("'Combine' not selected - select 'Intersection' or 'Union'");
               }
                  
               if (request.getParameter("combine").equals("Intersection")) {
                  //add as AND to previous
                  criteria = new Intersection(criteria, newCriteria);
               }
               else if (request.getParameter("combine").equals("Union")) {
                  //add as OR to previous
                  criteria = new Union(criteria, newCriteria);
               }
               else  {
                  out.write("ERROR: Unknown value for 'Combine':"+request.getParameter("combine"));
                  out.flush();
                  throw new IllegalArgumentException("Unknown value for 'Combine':"+request.getParameter("combine"));
               }
            }
         }
         
      }
      //build up list of search tables - ie the scope
      //this consists of any tables in either searchCols and resultCols
      Vector searchTables = new Vector();
      for (int i = 0; i < searchCols.length; i++) {
         if (searchCols[i].indexOf(".") >-1) {
            String table = searchCols[i].substring(0, searchCols[i].indexOf("."));
            if (!searchTables.contains(table)) {
               searchTables.add(table);
            }
         }
      }
      
      
   
      //build up list of output columns
      Vector outColRefs = new Vector();
      if (!resultCols[0].trim().equals("*")) { //if it's not a 'select *'
         for (int i = 0; i < resultCols.length; i++) {
            String colRef = resultCols[i];
            if ((colRef != null) && (colRef.length() >0)) {
               String tableName = "";
               String colName = "";
               if (colRef.indexOf(".")==-1) {
                  colName = colRef;
               }
               else {
                  //split table/column
                  tableName = colRef.substring(0, colRef.indexOf("."));
                  colName = colRef.substring(colRef.indexOf(".")+1);
               }
               outColRefs.add(new ColumnReference(tableName, colName));
      
               if (!searchTables.contains(tableName)) {
                  searchTables.add(tableName);
               }
      
            }
         }
      }
      ReturnSpec resultsDef = null;
      if (outColRefs.size() > 0) {
         resultsDef = new ReturnTable(null, (NumericExpression[]) outColRefs.toArray(new NumericExpression[] {}));
      }
      else {
         resultsDef = new ReturnTable(null); //all columns
      }
   
      Query query = new Query( (String[]) searchTables.toArray(new String[] {}), criteria, resultsDef);
            
      String comment = "Generated from queryBuilder '"+request.getRequestURI()+"'"; //doesn't give complete URL... :-(
      if (request.getParameter("MakeAdql074") != null) {
         adqlXml = Adql074Writer.makeAdql(query, comment);
      }
   //   else if (request.getParameter("MakeAdql05") != null) {
   //      adqlXml = Query2Adql05.makeAdql(query, comment);
   //   }
      else {  //default to makign latest
         adqlXml = Adql074Writer.makeAdql(query, comment);
      }
      out.flush();
   }
   catch (Throwable th) {
      LogFactory.getLog("JSP").error(th+" Making ADQL/xml from query builder",th);
      out.write(ServletHelper.exceptionAsHtml("Making ADQL/xml from Query Builder",th,""));
      
   }

%>
<form action='adqlXmlForm.jsp'>
  <textarea name="AdqlXml" rows="5" cols="100"><%= adqlXml %></textarea>
  <hr>
  ...Done.  Please press Next
  <input type='submit' value='Next'>
</form>

<%-- jsp:forward page="adqlXmlForm.jsp" >
   <jsp:param name="AdqlXml" value="<%= adqlXml %>" />
</jsp:forward>
--%>
</body>
</html>

