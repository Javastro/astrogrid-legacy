<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Delete File</title>
</head>

<body>
<h1>Delete File</h1>

<p>
Use this page to delete a file or container from MySpace.
</p>


<form action="deleteFile.jsp" method="POST">
<table>
  <tr>
    <td>File:</td>
    <td><input name="file" type="text"
      value="/clq/copyfile" size=40></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="deleteFile"></td>
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
This page can be used to delete either files or containers.  However,
containers can be deleted only if they are empty.
</p>

<p>
You should supply the full MySpace name of the file or container, with
a complete container path.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
