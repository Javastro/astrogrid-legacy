<%@ page import="org.astrogrid.datacenter.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Publisher's Astrogrid Library for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Welcome to PAL - the Publisher's Astrogrid Library.  This is a standard AstroGrid
datacenter interface to publish a set of data to the Virtual Observatory.
</p>
<p> To query that data using this interface, go to the 'Ask Query' on the left.
</p>

<h1>Install Notes</h1>
<p>
If you have just installed PAL, then you can now run some
<a href="self-test.jsp">self-tests</a> to run diagnostics, or
read how to <a href="configuration.html">configure</a> your datacenter.
</p>

</body>
</html>

