<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Import Contents</title>
</head>

<body>
<h1>Import Contents</h1>

<p>
Use this page to import a file into MySpace.  You type the contents of
the file into a text box, below.
</p>


<form action="importContents.jsp" method="POST">
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
      value="/clq@lei/serv1/workflow/newfile" size=40></td>
  </tr>

  <tr>
    <td>Contents:</td>
    <td><textarea name="contents"
          size="60" rows="10">The Snark was a Boojum, you see.</textarea></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="button" VALUE="importContents"></td>
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
You should supply the full MySpace name of the destination file or
container, with a complete container path.
</p>

<p>
Using the text box above it is only feasible to type in a relatively
short string for the file contents.  This restriction is purely a 
limitation of this Web page, which is intended for testing and
demonstration.  The underlying MySpace service can import arbitrarily
long strings and save them as MySpace files.
</p>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
