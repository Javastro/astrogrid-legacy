<%@ page import="java.io.*,
         org.astrogrid.dataservice.service.*,
         org.astrogrid.query.adql.*,
         org.astrogrid.query.sql.*,
         org.astrogrid.query.*"
   isThreadSafe="false"
   session="false"
%>

<%-- This bit of code translates SQL into ADQL; this is called from the
     adqlSqlForm page, but also might useful for people who want to store
     queries as URLs using SQL-like syntax --%>
<%
   String adqlSql = request.getParameter("AdqlSql");
   String adqlXml = request.getParameter("AdqlXml");
   if (adqlSql != null) {
      //if (request.getParameter("MakeAdql05") != null) {
      //   adqlXml = Sql2Adql05.translate(adqlSql);
     // }
      if (request.getParameter("MakeAdql074") != null) {
         adqlXml = Adql074Writer.makeAdql(SqlParser.makeQuery(adqlSql), "From SQL:"+adqlSql);
      }
      else {
         //default
         adqlXml = Adql074Writer.makeAdql(SqlParser.makeQuery(adqlSql), "From SQL:"+adqlSql);
      }
   }
%>
<jsp:forward page="adqlXmlForm.jsp" >
   <jsp:param name="AdqlXml" value="<%= adqlXml %>" />
</jsp:forward>

