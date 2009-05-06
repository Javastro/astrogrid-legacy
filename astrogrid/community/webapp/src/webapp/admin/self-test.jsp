<%@ page isThreadSafe="false" session="false"%>

<html>
<head>
<title>Community self-tests</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Testing your configuration</h1>

<ul>
  <li><a href="happyaxis.jsp">Tests of the Axis SOAP framework.</a></li>
  <li><a href="test?suite=org.astrogrid.community.webapp.PropertiesSelfTest">Tests on the context file.</a></li> 
</ul>
</div>

</body>
</html>
