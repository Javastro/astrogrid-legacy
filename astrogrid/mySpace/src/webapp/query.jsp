<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.*,
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

(This is not right yet - only does one layers of directories, needs to recurse)
<pre>
<%
  User operator = new User();
  StoreClient client = StoreDelegateFactory.createDelegate(operator, new Agsl("myspace:"+serviceURL));

  StoreFile fileRoot = client.getFiles(query);
  out.print(fileRoot.toString() );
  StoreFile[] files = fileRoot.listFiles();
  for (int i=0;i<files.length;i++) {
     out.print(files[i].toString());
  }
%>
</pre>


<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
