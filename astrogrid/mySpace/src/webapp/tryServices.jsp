<%@ page import="org.astrogrid.mySpace.delegate.*,
				 java.net.*,
				 java.util.*,
                 java.io.*"
   session="false" %>

<html>
<head>
<title>MySpace Web Services</title>
</head>
<body>
<h1>MySpace WebServices</h1>
<%
	URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/MySpaceManager");
%>
The end point for the service is: <%=serviceURL%> . <BR>
<%
	MySpaceClient client = MySpaceDelegateFactory.createDelegate(serviceURL.toString());
	
    String userId = "jdt"; //these will need to become member vars to persist
    String communityId = "roe";
    String credential = "any";
    String serversVector = "serv1";
    
    boolean createUserSubmitted = ("createUser".equals(request.getParameter("button"))); //depends on form request
    
    if (createUserSubmitted) {  
       userId = request.getParameter("userId"); //these are going to need to be local to retain
       communityId = request.getParameter("communityId");
       credential = request.getParameter("credential");
       serversVector = request.getParameter("servers");
    }
%>
<h2>Add User</h2>
<!-- Can this stuff be automated?  Does struts help?-->
<form action="tryServices.jsp" method="POST">
<table summary="what's this for?">
<tr><td>User id</td><td><input name="userId" type="text" value="<%=userId%>" size=20></td></tr>
<tr><td>Community id</td><td><input name="communityId" type="text" value="<%=communityId%>" size=20></td></tr>
<tr><td>Credential id</td><td><input name="credential" type="text" value="<%=credential%>" size=20></td></tr>
<tr><td>Servers id </td><td><input name="servers" type="text" value="<%=serversVector%>" size=30></td></tr>
<tr><td><INPUT TYPE="submit" NAME="button" VALUE="createUser"></td></tr>
</table>
</form>
<% 
//Detect whether this form has been submitted
if (createUserSubmitted) {
    //parse the string 
    Vector servers = new Vector();
    if (serversVector!=null) {
	    String[] serversList = serversVector.split(",");
	    for (int i=0;i<serversList.length;++i) {
	    	servers.add(serversList[i]);
	    }
	}  
    //get myspace delegate
	boolean result = client.createUser(userId, communityId, credential, servers);
	out.print("<h3>Result</h3>");
    out.print(result);
    out.print("<BR>");
}

%>



</body>