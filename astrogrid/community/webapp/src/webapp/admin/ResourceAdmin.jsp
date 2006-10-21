<%@ page import="org.astrogrid.community.common.policy.data.ResourceData,
                 org.astrogrid.community.server.policy.manager.PolicyManagerImpl,
                 org.astrogrid.community.server.policy.manager.ResourceManagerImpl"
    session="true" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
//ResourceManagerImpl rmi = new ResourceManagerImpl();
PolicyManagerImpl rmi = new PolicyManagerImpl();


String removeResource = request.getParameter("RemoveResource");
String addResource = request.getParameter("AddResource");
String editResource = request.getParameter("EditResource");
String info = "";
String ident = null;
ResourceData changeResource = null;
if(removeResource != null && removeResource.trim().length() > 0) {
	ident = request.getParameter("ident");
	ident = ident.trim();		
	rmi.delResource(ident);
	info = "Resource was deleted for id = " + ident;	
}else if(addResource != null && addResource.trim().length() > 0) {
	changeResource = rmi.addResource();
	changeResource.setDescription(request.getParameter("description"));
	rmi.setResource(changeResource);
	info = "Resource was added for id = " + changeResource.getIdent();		
}else if(editResource != null && editResource.trim().length() > 0) {
	ident = request.getParameter("ident");
	ident = ident.trim();		
	changeResource = new ResourceData(ident,request.getParameter("description"));
	rmi.setResource(changeResource);	
	info = "Resource was updated for id = " + ident;
}
Object[] resources = rmi.getResources();
if(resources != null) 
	System.out.println("the size of resources = " + resources.length);
%>

<html>
<head>
		<title>Resource Administration</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="beans.xml" %>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>


		<p>
			<strong><font color="blue"><%=info%></font></strong><br />
			Resource administration page, here you can add, edit, or delete accounts.

		<table>
			<tr>
				<td>
					Description
				</td>				
				<td>
					Add
				</td>
			</tr>		
			<tr>
				<form method="get" />
					<td>
						<input type="text" name="description" />
					</td>					
					<td>
						<input type="hidden" name="AddResource" value="true"/>				
						<input type="submit" name="AddResourcetSubmit" value="Add" />
					</td>
				</form>
			</tr>
		</table>		
		<br />
		<%
		if(resources != null && resources.length > 0) {
		%>
		List of resources:<br />
		<table>
			<tr>
				<td>
					Identifier
				</td>
				<td>
					Description
				</td>				
				<td>
					Edit
				</td>
				<td>
					Remove
				</td>
			</tr>
		<%
			}
			ResourceData rd = null;
			if(resources != null && resources.length > 0)
			for(int i = 0;i < resources.length;i++) {
				rd = (ResourceData)resources[i];
		%>
			<tr>
				<form method="get">
					<td>
						<%=rd.getIdent()%>
					</td>
					<td>
						<input type="text" name="description" value="<%=rd.getDescription()%>" />
					</td>					
					<td>
						<input type="hidden" name="ident" value="<%=rd.getIdent()%>" />
						<input type="hidden" name="EditResource" value="true" />
						<input type="submit" name="EditResourceSubmit" value="Edit" />
					</td>
				</form>
				<form method="get">				
					<td>
						<input type="hidden" name="RemoveResource" value="true" />
						<input type="hidden" name="ident" value="<%=rd.getIdent()%>" />
						<input type="submit" name="RemoveResourceSubmit" value="Remove" />
					</td>
				</form>				
			</tr>	
		<%
			}
		%>
		</table>
		<br />
		<a href="index.html">Administration Index</a>
		
		</p>	
	</body>	
</html>