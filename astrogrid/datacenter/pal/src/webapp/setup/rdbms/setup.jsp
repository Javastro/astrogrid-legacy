<%@ page import="org.astrogrid.datacenter.service.DataServer, org.astrogrid.webapp.SetProperties"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>RDBMS Setup for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../../header.xml" %>
<%@ include file="../common/navigation.xml" %>

<div id='bodyColumn'>

<h1>Basic RDBMS Setup</h1>
<p>
<form action='../SetProperties' method='post'>

  <input type='hidden' name='forwardTo' value='rdbms/setup.jsp'>

 <table>
 <tr>
  <%= SetProperties.makePropertyInput("datacenter.plugin", "Query Plugin", "org.astrogrid.datacenter.JdbcPlugin") %>
  <td>Test (Todo)</td>
 </tr>
 <tr>
  <%= SetProperties.makePropertyInput("datacenter.name", "Title of data service", "") %>
 </tr>
 </table>
 
</form>

</body>
</html>

