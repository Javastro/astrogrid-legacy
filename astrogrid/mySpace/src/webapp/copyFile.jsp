<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Copy File</title>
</head>

<body>
<h1>Copy File</h1>

<%
  String[] paramNames={"userId","communityId","credential","oldfile",
    "newfile"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String oldFile = request.getParameter("oldfile");
  String newFile = request.getParameter("newfile");

  String query = "/" + userId + "@" + communityId + "/*";
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

  String result = client.copyDataHolding(userId, communityId,
    credential, oldFile, newFile);
%>

<p>
The result from copyFile was:
</p>

<%
  out.print(result + "<BR>");
%>

<p>
The new state of account <%=query%> is:
</p>

<%
  Vector results = client.listDataHoldingsGen(userId, communityId,
    credential, query);

  int resultsSize = results.size();

  if (resultsSize > 0)
  {  for (int i=0; i<resultsSize; i++)
     {  out.print(results.elementAt(i) + "<BR>");
     }
  }
  else
  {  out.print("No entries satisfied the query." + "<BR>");
  }

%>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
