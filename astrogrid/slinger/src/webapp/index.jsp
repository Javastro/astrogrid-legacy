<%@ page
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Store Browser Service</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Welcome to the Store Browser Service.  This includes:
<p>
<ul>
<li>A <a href='Picker'>Picker</a> for 'picking' items from stores.  This picker page
can be used standalone as a 'lite' store browser, or linked from your own pages.
<li>(Todo) A <a href='Browser'>Browser</a> for a fully-featured web-based browser on
your storage.
<li>(Todo) Webstart-wrapped Swing GUI
<li>Slinger package.  A java API to describe and connect to various services, including
myspaces, homespaces, ftp servers, disk space, etc.
</ul>
<p>
The browser and picker pages are included in the Slinger package, so that you can
include them directly in your own web pages.

</body>
</html>

