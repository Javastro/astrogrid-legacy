<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 org.astrogrid.community.User,
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
  String[] paramNames={"file"};
  String file = request.getParameter("file");

%>

<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/Manager");
%>

<%
  User operator = new User();
  MySpaceIt05Delegate manager = new MySpaceIt05Delegate(operator,
    serviceURL.toString());

  manager.setThrow(false);
  manager.delete(file);
%>

<p>
The Manager returned the following messages:
</p>

<pre>
<%
  ArrayList statusList = manager.getStatusList();

  
  int numMessages = 0;
  
  if (statusList != null) numMessages = statusList.size();

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


</body>
</html>
