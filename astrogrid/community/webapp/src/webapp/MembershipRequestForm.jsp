<html>

<head>
<title>Request for membership of the community</title>
<style type="text/css" media="all">
  @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="contentColumn">
<h1>Request for membership of the community</h1>
<form action="MembershipRequestResult.jsp" method="post">
  <table>
    <tr>
      <td><label>Full name:</label></td>
      <td><input type="text" name="userCommonName"/></td>
    <tr>
      <td><label>Log-in name:</label></td>
      <td><input type="text" name="userLoginName"
    </tr>
    <tr>
      <td><label>Password:</label></td>
      <td><input type="password" name="userPassword"/>
    </tr>
    <tr>
      <td><label>Email address</label></td>
      <td><input type="text" name="userEmail"/>
    </tr>
    <tr>
      <td><input type="submit" value="Submit"/></td>
      <td/>
    </tr>
  </table>
</form>
</div>
</body>

</html>
      