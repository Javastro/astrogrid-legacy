<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>View Resource</h1>

<p>
Enter the Identifier for the entry you want to view.
</p>

<form action="viewEntryXml.jsp" method="post">
<p>
Resource identifier
 ivo://<input name="IVORN" type="text" value="" size="60"/>
 <input type="submit" name="button" value="Find"/>
</p>
<p>
Examples:<br/>
roe.ac.uk/DSA_6dF/rdbms<br/>
org.astrogrid.test/registry
</p>
</form>


</body>
</html>

