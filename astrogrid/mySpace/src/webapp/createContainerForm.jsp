<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Create Container</title>
</head>

<body>
<h1>Create Container</h1>

<p>
Use this page to create a new container.
</p>


<form action="createContainer.jsp" method="POST">
<table>

  <tr>
    <td>Container:</td>
    <td><input name="container" type="text"
      value="/clq/newcontainer" size=30></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="createContainer"></td>
    <td></td>
  </tr>

</table>
</form>

<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/Manager");
%>

<p>
The end point for this service is: <%=serviceURL%>
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

<hr>

<h2>Notes</h2>

<p>
You should supply the full, absolute container structure of the new
container.
</p>

<p>
MySpace containers have a hierarchical structure analogous to Unix
directories.  The top level of your container hierarchy corresponds
to your MySpace account.  Usually you will be unable to create or delete 
top level containers.  However, you can create (and delete) containers
further down the hierarchy as you wish.  A few containers are created 
automatically when your MySpace account is created (eg. <code>query</code> 
and <code>workflow</code>).  Other components of the AstroGrid system use 
these containers and it is unwise to delete them.  Usually you will be
 unable to create or delete containers in another user's MySpace.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
