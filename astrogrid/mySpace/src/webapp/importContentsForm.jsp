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
    <td><input name="userId" type="text" value="avodemo" size=20></td>
  </tr>

  <tr>
    <td>Community Identifier:</td>
    <td><input name="communityId" type="text" value="test.astrogrid.org" size=20></td>
  </tr>

  <tr>
    <td>Credential:</td>
    <td><input name="credential" type="text" value="any" size=20></td>
  </tr>

  <tr>
    <td>File:</td>
    <td><input name="file" type="text"
      value="/avodemo/working/SnarkNotes.txt" size=40></td>
    <td>Give full MySpace name of the destination file including container path.</td>

  </tr>

  <tr>
    <td>Contents:</td>
    <td><textarea name="contents"
          size="60" rows="10">The Snark was a Boojum, you see.</textarea></td>
<td>
Using this text box it is only feasible to type in a relatively
short string for the file contents.  This restriction is purely a
limitation of this Web page, which is intended for testing and
demonstration.  The underlying MySpace service can import arbitrarily
long strings and save them as MySpace files.
</td>
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



</body>
</html>
