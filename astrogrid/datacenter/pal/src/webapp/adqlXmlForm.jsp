<%@ page import="java.io.*,
         org.astrogrid.datacenter.service.*,
         org.astrogrid.datacenter.sqlparser.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Query Form for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/maven-base.css");
          @import url("./style/maven-theme.css");
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
      <form action="submitAdqlXml.jsp" method="POST">
        <textarea name="AdqlXml" rows="20" cols="100%"><%= request.getParameter("AdqlXml") %></textarea>
        <p/>
        <%@ include file='resultsForm.xml' %>
        <p>
        <input type="submit" value="Submit ADQL" />
        </p>
      </form>
      (<a href="adqlSqlForm.jsp">Translate from SQL</a>)
      (<a href="adql">Sample queries</a>)
      <i>Note that some browsers will modify viewed XML, so check for DEFANGED, case changes, etc</i>
      <br />
    </p>
</div>
</body>
</html>

