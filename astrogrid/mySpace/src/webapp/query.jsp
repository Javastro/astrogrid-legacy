<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 org.astrogrid.community.User,
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
  String[] paramNames={"query"};
  String query = request.getParameter("query");
%>


<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/Manager");
%>

<p>
The end point for this service is: <%=serviceURL%>
</p>

<p>
The following entries satisfied query <code><%=query%></code>:
</p>

<pre>
<%
  User operator = new User();
  MySpaceIt05Delegate manager = new MySpaceIt05Delegate(operator,
     serviceURL.toString());

  manager.setThrow(false);
  EntryNode fileRoot = (EntryNode)manager.getFiles(query);
  out.print(fileRoot.toString() );
%>
</pre>

<p>
The Manager returned the following messages:
</p>

<pre>
<%
  ArrayList statusList = manager.getStatusList();

  int numMessages = statusList.size();

  if (numMessages > 0)
  {  for(int loop=0; loop<numMessages; loop++)
     {  StatusMessage message =
          (StatusMessage)statusList.get(loop);
        out.println(message.toString() );
     }
  }
  else
  {  out.print("No messages returned.");
  }

%>
</pre>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
