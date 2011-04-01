<%@ page isThreadSafe="false" session="false"%>
<% String pathPrefix = "."; // For the navigation include %>

<html>
<head>
<title>Table and column metadata</title>
<style type="text/css" media="all">
  @import url("./style/astrogrid.css");
</style>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Table and column metadata</h1>

<%= new org.astrogrid.tableserver.metadata.TableMetaDocRenderer().renderMetaDoc() %>

</div>

   <%@ include file="footer.xml" %>
</body>
</html>


