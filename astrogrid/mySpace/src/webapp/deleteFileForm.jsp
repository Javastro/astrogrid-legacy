<%@ page import="org.astrogrid.mySpace.delegate.*,
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
      value="/clq@lei/serv1/copyfile" size=40></td>
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
