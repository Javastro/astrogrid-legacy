<%@ page import="org.astrogrid.mySpace.delegate.*,
                 org.astrogrid.mySpace.delegate.helper.Assist,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Delete File</title>
</head>

<body>
<h1>Delete File</h1>

<%
  String[] paramNames={"userId","communityId","credential","file"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String file = request.getParameter("file");

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

  String result = client.deleteDataHolding(userId, communityId,
    credential, file);
%>

<p>
The result from deleteFile was:
</p>

<pre>
<%
  Assist assistant = new Assist();
  String displayString = assistant.formatTree(result);
  out.print(displayString);
%>
</pre>

<p>
The new state of account <%=query%> is:
</p>

<pre>
<%
  Vector results = client.listDataHoldingsGen(userId, communityId,
    credential, query);

  int resultsSize = results.size();

  if (resultsSize > 0)
  {  for (int i=0; i<resultsSize; i++)
     {  String xmlString = (String)results.elementAt(i);
        Vector summaryList = assistant.getDataItemSummary(xmlString);

        int numEntries = summaryList.size();

        for (int loop = 0; loop < numEntries; loop++)
        {  out.print((String)summaryList.elementAt(loop) + "\n");
        }
     }
  }
  else
  {  out.print("No entries satisfied the query." + "<BR>");
  }
%>
</pre>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
