<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Export Contents</title>
</head>

<body>
<h1>Export Contents</h1>

<p>
Use this page to export the contents of a MySpace file.  The contents
of the file are listed in the page of results.
</p>


<form action="exportContents.jsp" method="POST">
<table>
  <tr>
    <td>File:</td>
    <td><input name="file" type="text"
      value="/clq/workflow/newfile" size=40></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="exportContents"></td>
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
You should supply the full MySpace name of the file, with a complete
container path.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
