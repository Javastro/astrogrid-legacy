<%@ page import="org.astrogrid.mySpace.delegate.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Import Contents</title>
</head>

<body>
<h1>Import Contents</h1>

<%
  String[] paramNames={"userId","communityId","credential","file",
    "contents"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String file = request.getParameter("file");
  String contents = request.getParameter("contents");

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

  boolean ok = client.saveDataHolding(userId, communityId,
    credential, file, contents, "workflow", "");


  if (ok)
  {  out.print("File " + file + " created successfully. <BR>");
  }
  else
  {  out.print("Failed to create file " + file + ". <BR>");
  }
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
