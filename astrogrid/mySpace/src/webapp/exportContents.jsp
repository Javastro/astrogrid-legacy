<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Export Contents</title>
</head>

<body>
<h1>Export Contents</h1>

<%
  String[] paramNames={"userId","communityId","credential","file"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String file = request.getParameter("file");
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

  String result = client.getDataHolding(userId, communityId,
    credential, file);
%>

<p>
The contents of file <code><%=file%></code> are:
</p>

<pre>
<%
  out.print(result);
%>
</pre>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
