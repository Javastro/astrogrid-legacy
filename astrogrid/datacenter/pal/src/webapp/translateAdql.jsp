<%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       java.io.*,
       org.astrogrid.datacenter.sqlparser.*,
       org.apache.commons.logging.*,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.datacenter.service.HtmlDataServer,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
%>
<%
   String adqlSql = request.getParameter("AdqlSql");
   String adqlXml = "";
   if (adqlSql == null) {
      adqlSql = "";
   }
   else {
      if (request.getParameter("MakeAdql05") != null) {
         adqlXml = Sql2Adql05.translate(adqlSql);
      }
      else if (request.getParameter("MakeAdql074") != null) {
         adqlXml = Sql2Adql074.translate(adqlSql);
      }
      else {
         adqlXml = "ERROR: don't know which version of ADQL to translate to";
      }
   }
%>
<html>
<head>
<title>ADQL/sql Translator Form</title>
<%=HtmlDataServer.getCssLink() %>
<link rel="stylesheet" href="default.css" type="text/css">
</head>
<body>
  <%=HtmlDataServer.getPageHeader() %>
  <h1>Translate ADQL/sql to ADQL/xml</h1>
  <p>
   <form action="translateAdql.jsp" method="GET">
    <p>
      <table><tr>
        <td>ADQL/sql</td>
        <td><textarea name="AdqlSql" rows='1' cols='100'><%=adqlSql %></textarea></td>
      </tr></table>
    </p>
    <p>
       <input type='submit' name='MakeAdql05' value='Make ADQL v0.5' />
       <input type='submit' name='MakeAdql074' value='Make ADQL v0.7.4' />
    </p>
    <p>
      <table><tr>
        <td>ADQL/xml</td>
        <td><textarea name="AdqlXml" rows='50' cols='100'><%=adqlXml %></textarea></td>
      </tr></table>
    </p>
   </form>
  </p>
  <%=HtmlDataServer.getPageFooter() %>
</body>
</html>

