<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.*,
                 org.astrogrid.community.User,
                 org.astrogrid.io.Piper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>View File</title>
</head>

<%
//  String[] paramNames={"userId","communityId","credential","file"};
  User user = User.ANONYMOUS; //new user(request.getParameter("userId"),
               //      request.getParameter("communityId"),
               //      request.getParameter("credential"));
  String file = request.getParameter("file");
%>

<body>
<h1><%=file%></h1>

<%
  URL serviceURL = new URL ("http", request.getServerName(),
         request.getServerPort(),
         request.getContextPath() + "/services/Manager");
%>

<pre>
<%
  StoreClient client = StoreDelegateFactory.createDelegate(user, new Agsl("myspace:"+serviceURL));

  Reader reader = new InputStreamReader(client.getStream(file));

  Piper.bufferedPipe(reader, out);
%>
</pre>

</body>
</html>
