<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Upload File</title>
</head>

<body>
<h1>Upload File</h1>

<p>
Use this page to load a file from your local hard disk into MySpace.
</p>

<form action="uploadFile.jsp" method="POST" ENCTYPE="multipart/form-data">
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
    <td>MySpace target filename:</td>
    <td><input name="targetFile" type="text"
      value="/avodemo/NewFile.txt" size=40></td>
    <td>Give full MySpace name of the destination file including container path.</td>

  </tr>

  <tr>
    <td>Local source filename:</td>
    <td><input name="sourceFile" type="file"size=40></td>
  </tr>

  <tr>
    <td><INPUT TYPE="submit" NAME="uploadButton" VALUE="Upload"></td>
    <td></td>
  </tr>

</table>
</form>

</body>
</html>
