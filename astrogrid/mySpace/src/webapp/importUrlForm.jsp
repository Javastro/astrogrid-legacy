<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Import URL</title>
</head>

<body>
<h1>Import URL</h1>

<p>
Use this page to import a file into MySpace.  You supply a URL for the
file in a text box, below.
</p>


<form action="importUrl.jsp" method="POST">
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
    <td>File:</td>
    <td><input name="file" type="text"
      value="/clq@lei/serv1/workflow/webfile" size=40></td>
  </tr>

  <tr>
    <td>URL:</td>
    <td><input name="url" type="text"
      value="http://www.roe.ac.uk/acdwww/frequent.html" 
      size=60></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="importUrl"></td>
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
You should supply the full MySpace name of the destination file or container,
with a complete container path.

</p>

<p>
Type a valid URL into the appropriate text box.  The file referenced
by the URL will be retrieved and stored in MySpace.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
