<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Create User</title>
</head>

<body>
<h1>Create User</h1>

<p>
Use this page to add a new user to the MySpace service.
</p>


<form action="createUser.jsp" method="POST">
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
    <td><input name="server" type="hidden" value="serv1">
      <INPUT TYPE="submit" NAME="button" VALUE="createUser"></td>
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

</body>
</html>

