<%@ page import="org.astrogrid.datacenter.service.DataServer, org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> </title>
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
Welcome to the PAL interface to <%=DataServer.getDatacenterName() %>.  PAL is the
Publisher's Astrogrid Library and gives Virtual Observatory compliant access to
this dataset.
</p>
<p> To query this data directly, you can use the 'Ask Query' on the left.
</p>
<h2><%=DataServer.getDatacenterName() %></h2>
<p><%= SimpleConfig.getSingleton().getString("datacenter.description","(No description given)") %>
</p>
<h1>Install Notes</h1>
<p>
If you have just installed PAL, then you can now run some
<a href="installChecks.jsp">checks</a> to run diagnostics, or
read how to <a href="docs/configure.jsp">configure</a> your datacenter.
</p>

</body>
</html>

