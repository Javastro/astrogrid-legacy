<%@ page import="org.astrogrid.mySpace.delegate.*,
                 org.astrogrid.mySpace.delegate.helper.Assist,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Create User</title>
</head>

<body>
<h1>Create User</h1>

<%
  String[] paramNames={"userId","communityId","credential","server"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String server = request.getParameter("server");
  Vector servers = new Vector();
  servers.add(server);
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
  boolean ok = client.createUser(userId, communityId, credential, servers);
%>

<p>
The result from createUser was <%=ok%>.
</p>

<p>
The following containers have been created for user
<code><%=userId%>@<%=communityId%></code>:
</p>

<pre>
<%
  String query = "/" + userId + "@" +communityId +  "/*";

  Vector results = client.listDataHoldingsGen(userId, communityId,
    credential, query);

  int resultsSize = results.size();

  if (resultsSize > 0)
  {  for (int i=0; i<resultsSize; i++)
     {  String xmlString = (String)results.elementAt(i);
        Assist assistant = new Assist();
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
