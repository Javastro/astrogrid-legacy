<%@ page import="org.astrogrid.mySpace.delegate.*,
				 java.net.*,
				 java.util.*,
                 java.io.*"
   session="false" %>


<html><head><title/></head>
<body>
<%
String[] paramNames={"userId","communityId","credential","servers"};
String userId = request.getParameter("userId");
String communityId = request.getParameter("communityId");
String credential = request.getParameter("credential");
Vector servers = null; //request.getParameter("servers");
%>
The stuff you typed was: <BR>
<%
	for (int i=0;i<paramNames.length;++i) {
		out.println(request.getParameter(paramNames[i])+"<br>");
	}
%>

<%
	URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/MySpaceManager");
%>
The end point for the service is: <%=serviceURL%> . <BR>
<%
	MySpaceClient client = MySpaceDelegateFactory.createDelegate(serviceURL.toString());
	boolean ok = client.createUser(userId, communityId, credential, servers);
%>
Result from createUser was <%=ok%>.
</body>
</html>