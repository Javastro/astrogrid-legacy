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
String server = request.getParameter("servers");
Vector servers = new Vector();
servers.add(server);
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
<P>
Attempting to list holdings:<BR>
<%
	String query = "*";
	Vector results = client.listDataHoldings(userId, communityId, credential, query);
	int resultSize = results.size();
	for (int i=0; i<resultSize;++i) {
		out.print(results.elementAt(i)+"<BR>");
	}
	
	String message="http://www.google.com";
	boolean ok3 = client.saveDataHoldingURL(userId, communityId, credential, "/jdt@roe/serv1/fred", message, "VOtable", "");
	out.print("Result of saveDataHolding: " + ok3 + "<BR>");	
	
	//String query = "*";
	results = client.listDataHoldings(userId, communityId, credential, query);
	resultSize = results.size();
	for (int i=0; i<resultSize;++i) {
		out.print(results.elementAt(i)+"<BR>");
	}
%>
</body>
</html>