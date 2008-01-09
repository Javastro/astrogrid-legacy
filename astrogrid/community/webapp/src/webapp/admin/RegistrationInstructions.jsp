<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="true"
%>

<html>
    <head>
        <title>Service registration</title>
        <style type="text/css" media="all">
            @import url("../style/astrogrid.css");
        </style>
        </title>
    </head>
<body>
  
<%@ include file="beans.xml" %>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Registration</h1>

<p>
When setting up community for the first time, you need to register 
it in an IVO resource-registry. For this community version there are two
kinds of registration.
</p>

<ul>
  <li>
    <a href="OldStyleRegistrationForm.jsp">Old-style registration</a>:
    works with client software up to and including version 2007.2.</li>
    <li>
      <a href="NewStyleRegistrationProcedure.jsp">New-style registration</a>:
      works with client software of version 2008.0 and later.
    </li>
</ul>
<p>
  You should make both kinds of registration, since both versions of client
  software are in circulation.
</p>

</body>
</html>
