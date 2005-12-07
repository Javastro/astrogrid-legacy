<%@ page import="java.io.*,
         java.net.*,
         org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> Query Form</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

  <h1>Submit an ADQL/xml Query to <%=DataServer.getDatacenterName() %></h1>
  <p>
  <b>Enter your ADQL/xml query here:</b>
      <form action="BrowserAskQuery" method="POST">
        <input type='hidden' name='UserName' value='JSP'>
        
        <i>Note that some browsers will modify viewed XML, so check for DEFANGED, case changes, etc</i>
        <textarea name="AdqlXml" rows="20" cols="100%"><%= request.getParameter("AdqlXml") %></textarea>
        <p/>
        <%@ include file='resultsForm.xml' %>
        <p>
        <input type="submit" value="Submit ADQL" />
        </p>
      </form>
      <a href="adqlSqlForm.jsp">Translate from SQL</a> *
      <a href="http://openskyquery.net/AdqlTranslator/Convertor.aspx">NVO Translator</a> 
      <!--
      *
      <a href="adql">Sample queries</a>
      -->
      <br />
    </p>
</div>
</body>
</html>

