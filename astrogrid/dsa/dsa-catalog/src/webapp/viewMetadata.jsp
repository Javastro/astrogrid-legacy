<%-- a JSP that displays the metadata in a form suitable for users.  This could/should
  well be an Xslt - I just can't be bothered making one, this is easier for me... --%>
<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.ucd.Ucd1Dictionary,
       org.astrogrid.units.UnitDictionary,
       org.astrogrid.tableserver.jdbc.RdbmsTableMetaDocGenerator,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.tableserver.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = "."; // For the navigation include %>

<html>
<head>
<title>MetaDoc for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>MetaDoc for <%=DataServer.getDatacenterName() %></h1>

<%= new TableMetaDocRenderer().renderMetaDoc() %>

</div>

   <%@ include file="footer.xml" %>
</body>
</html>


