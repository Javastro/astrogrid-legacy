<html>
<head>
<title>MySpace Web Services</title>
</head>
<body>
<h1>MySpace WebServices</h1>
<h2>Add User</h2>
<!-- Can this stuff be automated?  Make this page automatically redirect to correct place on web server -->
<form action="createUser.jsp" method="POST">
<table summary="what's this for?">
<tr><td>User id</td><td><input name="userId" type="text" value="jdt" size=20></td></tr>
<tr><td>Community id</td><td><input name="communityId" type="text" value="roe" size=20></td></tr>
<tr><td>Credential id</td><td><input name="credential" type="text" value="any" size=20></td></tr>
<tr><td>Servers id </td><td><input name="servers" type="text" value="serv1" size=30></td></tr>
<tr><td><INPUT TYPE="submit" NAME="button" VALUE="createUser"></td></tr>
</table>
</form>




</body>