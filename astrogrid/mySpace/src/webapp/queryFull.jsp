<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Query (Full Form)</title>
</head>

<body>
<h1>Query (Full Form)</h1>

<%
  String[] paramNames={"userId","communityId","credential","query"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String query = request.getParameter("query");
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

  Vector results = client.listDataHoldingsGen(userId, communityId,
    credential, query);
%>

<p>
The following entries satisfied query <code><%=query%></code>:
</p>

<%
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
