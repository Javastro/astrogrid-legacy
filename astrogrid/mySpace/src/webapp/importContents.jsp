<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.community.User,
                 org.astrogrid.store.*,
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
  User user = new User(request.getParameter("userId"), request.getParameter("communityId"), "dummyToken");
  String file = request.getParameter("file");
  String contents = request.getParameter("contents");

  String query = "/" + user.getUserId() + "/*";
%>


<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/Manager");
%>

<p>
The end point for this service is: <%=serviceURL%>
</p>

<%
  StoreClient client = StoreDelegateFactory.createDelegate(user, new Agsl("myspace:"+serviceURL.toString()));

  client.putString(contents, file, false);

%>

<p>
The new state of account <%=query%> is:
</p>

<pre>
<%
  StoreFile[] files = client.listFiles(query);

  int resultsSize = files.length;

  if (resultsSize > 0)
  {  for (int i=0; i<resultsSize; i++)
     {
           out.print( files[i].toString() + "\n");
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
