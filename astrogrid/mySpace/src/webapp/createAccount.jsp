<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 org.astrogrid.community.User,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Create Account</title>
</head>

<body>
<h1>Create Account</h1>

<%
  String[] paramNames={"accountId"};
  String accountId = request.getParameter("accountId");
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
  User operator = new User();
  MySpaceIt05Delegate manager = new MySpaceIt05Delegate(operator,
     serviceURL.toString());

  User account = new User(accountId, "", "", "");

  manager.setThrow(false);
  manager.createUser(account);
%>

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


<a href="listFiles.jsp">List Files</a>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
