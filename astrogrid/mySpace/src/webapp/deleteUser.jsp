<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Delete User</title>
</head>

<body>
<h1>Delete User</h1>

<%
  String[] paramNames={"userId","communityId","credential"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
%>


<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/MySpaceManager");
%>

<p>
The end point for this service is: <%=serviceURL%>
</p>

<%
  MySpaceClient client = MySpaceDelegateFactory.createDelegate(
    serviceURL.toString());
  boolean ok = client.deleteUser(userId, communityId, credential);

  if (ok)
  {  out.print("User " + userId + "@" + communityId +
       " deleted successfully. <BR>");
  }
  else
  {  out.print("Failed to delete user " + userId + "@" + communityId +
       ". <BR>");
  }
%>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
