<%@ page import="java.io.*,
         org.astrogrid.datacenter.service.*,
         org.astrogrid.datacenter.query.*"
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
         adqlXml = Query2Adql074.makeAdql(SqlQueryMaker.makeQuery(adqlSql));
      }
      else {
         //default
         adqlXml = Query2Adql074.makeAdql(SqlQueryMaker.makeQuery(adqlSql));
      }
   }
%>
<jsp:forward page="adqlXmlForm.jsp" >
   <jsp:param name="AdqlXml" value="<%= adqlXml %>" />
</jsp:forward>

