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
<html>
<head>
<title>MetaDoc for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>MetaDoc for <%=DataServer.getDatacenterName() %></h1>
<%
   VoDescriptionServer.clearCache(); //force refresh
   //Document voDescription = VoDescriptionServer.getVoDescription();
%>

<%= new TableMetaDocRenderer().renderMetaDoc() %>

<h1>Raw Metadata</h1>

<p>Download the raw metadata xml document <a href='GetMetadata'>here</a></p>
<%-- <p>If you have access to administrator functions, you can download regenerated metadata <a href='admin/generateMetadata.jsp'>here</a></p> --%>

</div>

   <%@ include file="footer.xml" %>
</body>
</html>


