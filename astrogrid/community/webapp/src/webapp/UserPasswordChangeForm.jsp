<html>

<head>
<title>Changing your community password</title>
<style type="text/css" media="all">
  @import url("style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="bodyColumn">
<h1>Changing your community password</h1>
<form action="password" method="post">
  <table>
    <tr>
      <td>Your log-in name</td>
      <td><input type="text" name="userLoginName"/></td>
    </tr>
    <tr>
      <td>Your current password</td>
      <td><input type="password" name="oldPassword"/>
    </tr>
    <tr>
      <td>Your new password (at least seven characters long)</td>
      <td><input type="password" name="newPassword"/>
    </tr>
    <tr>
      <td><input type="submit" value="Change password"/>
  </table>
</form>
</div>

</body>
</html>