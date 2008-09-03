<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
    <head>
        <title>Service registration</title>
<%@ include file="../inc/header.jsp" %>

<div id='bodyColumn'>

<h1>Registration</h1>

<p>
When setting up an application server, you need to register 
it in an IVO resource-registry. For this server version there are two
kinds of registration.
</p>

<ul>
  <li>
    <a href="../cec-http?method=register">Old-style registration</a>:
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
</div>
<%@ include file="../inc/header.jsp" %>
