<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.astrogrid.datacenter.metadata.*,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Metadata Editor for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../style/maven-base.css");
          @import url("../style/maven-theme.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%--don't include this as the links aren't right include file='../navigation.xml' --%>

<div id='bodyColumn'>
<h1>Metadata Administrator Menu for <%=DataServer.getDatacenterName() %></h1>

<ul>
   <li><a href='../viewMetadata.jsp'>View Metadata (HTML)</a>
   <li>Get Metadata (XML)
   <li>Get Metadata Resource:
   <ul>
      <li><a href='../GetMetadata?Resource=AuthorityType'>AuthorityType</a>
      <li><a href='../GetMetadata?Resource=RdbmsMetadata'>RdbmsMetadata</a>
      <li><a href='../GetMetadata?Resource=CEA'>CEA </a>
   </ul>
   <li><a href='pushMetadata.jsp'>Push Metadata to Registry</a>
</ul>
</div>

<%@ include file='../footer.xml' %>
</body>
</html>


