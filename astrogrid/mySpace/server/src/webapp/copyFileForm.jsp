<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Copy File</title>
</head>

<body>
<h1>Copy File</h1>

<p>
Use this page to copy a file within the MySpace service.
</p>


<form action="copyFile.jsp" method="POST">
<table>
  <tr>
    <td>Old File:</td>
    <td><input name="oldfile" type="text"
      value="/clq/workflow/newfile" size=40></td>
  </tr>

  <tr>
    <td>New File:</td>
    <td><input name="newfile" type="text"
      value="/clq/copyfile" size=40></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="copyFile"></td>
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
For both the old and new files you should supply full MySpace names,
with complete container paths.
</p>

<p>
Obviously, the old file should exist, but the new one should not.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
