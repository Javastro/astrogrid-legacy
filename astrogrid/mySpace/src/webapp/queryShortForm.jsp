<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Query (Short Form)</title>
</head>

<body>
<h1>Query (Short Form)</h1>

<p>
Use this page to browse the entries in the MySpace service.  The results
are shown using the `short-form', in which only the file name, and not
the preceding container structure, is shown.
</p>


<form action="queryShort.jsp" method="POST">
<table>
  <tr>
    <td>User Identifier:</td>
    <td><input name="userId" type="text" value="clq" size=20></td>
  </tr>

  <tr>
    <td>Community Identifier:</td>
    <td><input name="communityId" type="text" value="lei" size=20></td>
  </tr>

  <tr>
    <td>Credential:</td>
    <td><input name="credential" type="text" value="any" size=20></td>
  </tr>

  <tr>
    <td>Query:</td>
    <td><input name="query" type="text" value="/clq@lei/*" size=20></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="queryShort"></td>
    <td></td>
  </tr>

</table>
</form>

<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/MySpaceManager");
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
all the files and containers belonging to user <code>clq@lei</code> enter:
</p>

<quote>
<code>/clq@lei/*</code>
</quote>

<p>
or to see all the files in that user's <code>query</code> container enter:
</p>

<quote>
<code>/clq@lei/query/*</code>
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
