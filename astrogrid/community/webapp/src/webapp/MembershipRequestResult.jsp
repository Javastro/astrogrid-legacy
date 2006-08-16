<html>

<head>
<title>Result of membership request</title>
<style type="text/css" media="all">
  @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="contentColumn">
<h1>Result of membership request</h1>
<p>
You applied to join this community under the name
<%=request.getParameter("userCommonName")%>,
with log-in name
<%=request.getParameter("userLoginName")%>.
You will be notified by email to 
<%=request.getParameter("userEmail")%>
when the request has been checked by the community operator.
</p>
</div>
</body>

</html>