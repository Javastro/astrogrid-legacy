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
   contentType="text/xml"
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
<title>ADQL/sql Translator Form</title>
</html>
<body>
  <h1>"Translate ADQL/sql to ADQL/xml"</h1>
    <p>
      <form action="adqls-translator.jsp" method="GET">
       <p>
         ADQL/sql <input type="textarea" name="AdqlSql" rows='1' cols='100%' value='<%=adqlSql %>'/><br />
       </p>
       <p>
          <input type="submit" value="Translate" />
       </p>
       <p>
         ADQL/xml <input type="textarea" name="AdqlXml" rows='50' cols='100%' value='<%=adqlXml %>' />

       <input type='submit' name='MakeAdql05' value='Make ADQL v0.5'>
       <input type='submit' name='MakeAdql074' value='Make ADQL v0.7.4'>

      </form>
    </p>
  </h1>
</body>
</html>

