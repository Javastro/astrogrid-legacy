<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Slinger WebNode</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>

<div id='bodyColumn'>

   <h1>Slinger WebNode</h1>
   <p>
   Welcome to the 'Slinger' application.  'Slinger' is a library for resolving and
   routing data streams; this webapplication is a 'slinger webnode', and provides
   some servlets and a convenience page for resolving IVO resource names for stores.
   </p>
   <p>This webapp resolves using:
   <ul>
      <li><a href=''><%= SimpleConfig.getProperty("org.astrogrid.registry.query.endpoint","(not set)") %></a>
      <li><a href=''><%= SimpleConfig.getProperty("org.astrogrid.registry.query.altendpoint","(not set)") %></a>
   </ul>
   </p>

   <h2>Resolver</h2>
   <p>
   Enter the URI of the source below, and the contents of that source will be streamed
   to your browser.
   </p>
   <form action="Resolve" method="get">
       <p>
         <table border='0'>
         <tr><td align='right'>Source URI<td><input type="text" name="SourceUri" size='50'/></tr>
         </table>
       </p>
        <p>
          <input type="submit" name='Submit' value="Get"  />
        </p>
   </form>

</div>
</body>
</html>

