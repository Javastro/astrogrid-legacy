<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Query</title>
</head>

<body>
<h1>Query</h1>

<p>
Use this page to browse the entries in the MySpace service.
</p>


<form action="query.jsp" method="POST">
<table>

  <tr>
    <td>Query:</td>
    <td><input name="query" type="text" value="/clq/*" size=20></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="query"></td>
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
When you enter a query all the containers and files in the MySpace service
which match the query will be listed.  The matching is performed against
the name of the file, including its full container path.  It a complete
name is given it will be listed if a match is found.  However, a more
usual use is to include a wild-card (the asterisk, `*') at the end of the
query and all the matching entries will be listed.  For example, to see
all the files and containers belonging to user <code>clq</code> enter:
</p>

<quote>
<code>/clq/*</code>
</quote>

<p>
or to see all the files in that user's <code>query</code> container enter:
</p>

<quote>
<code>/clq/query/*</code>
</quote>

<p>
Simply entering <code>*</code> will show all the entries in the MySpace
service.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
