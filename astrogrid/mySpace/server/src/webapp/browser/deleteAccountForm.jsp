<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Delete Account</title>
</head>

<body>
<h1>Delete Account</h1>

<p>
Use this page to delete an account from the MySpace service.
</p>


<form action="deleteAccount.jsp" method="POST">
<table>
  <tr>
    <td>User Identifier:</td>
    <td><input name="accountId" type="text" value="clq" size=20></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="deleteAccount"></td>
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
This function will fail if the specified account does not exist.  If you
get an unexpected failure use the query function to check that the
specified account exists.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>

